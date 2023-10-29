package mipt.lecture7and8

import zio.*
import zio.logging.*

import java.io.IOException


object sheetA_FiberRef extends ZIOAppDefault:
    override val bootstrap = Runtime.removeDefaultLoggers >>> console(LogFormat.colored)


    private val tracksDone: Ref[Int] = Unsafe.unsafe { implicit unsafe =>
        Ref.unsafe.make(0)
    }

    private val currentTrack: FiberRef[Int] = Unsafe.unsafe { implicit unsafe =>
        FiberRef.unsafe.make(1)
    }


    val heavyMetalCalculation =
        currentTrack.locally(5):
            for 
                _       <- ZIO.logInfo("]-> starting")
                _       <- ZIO.sleep(Duration.fromMillis(1800))
                track   <- currentTrack.get
                _       <- ZIO.logInfo(s"]-> mixing $track")
                count   <- tracksDone.modify(prev => (prev+1, prev+1))
                _       <- ZIO.logInfo(s"]-> counting $count")
            yield "Manowar" -> "Warlord Returns"
        

    val hardRockCalculation =
        for 
            _       <- ZIO.logWarning("*-> starting")
            _       <- ZIO.sleep(Duration.fromMillis(500))
            track   <- currentTrack.modify(prev => (prev, prev+1)) // <- Increase counter
            _       <- ZIO.logWarning(s"*-> mixing $track")
            count   <- tracksDone.modify(prev => (prev+1, prev+1)) // <- Increase counter
            _       <- ZIO.logWarning(s"*-> counting $count")
        yield "Ozzy Osbourne" -> "Blizzard of Ozz"


    val punkIsNotDeadCalc =
        for
            _       <- ZIO.logError(">-> starting")
            _       <- ZIO.sleep(Duration.fromMillis(500))
            track   <- currentTrack.modify(prev => (prev, prev+1))
            _       <- ZIO.logError(s">-> mixing $track")
            count   <- tracksDone.modify(prev => (prev+1, prev+1))
            _       <- ZIO.logError(s">-> counting $count")
        yield "Offspring" -> "Smash"



    val common =
        for 
            _ <- Console.printLine("\t> Starting")

            fiber1 <- heavyMetalCalculation.fork
            fiber2 <- hardRockCalculation.repeatN(3).fork
            fiber3 <- punkIsNotDeadCalc.repeatN(1).fork

            _ <- Console.printLine("\t> Waiting")

            result1 <- fiber1.join
            result2 <- fiber2.join
            result3 <- fiber3.join

            _ <- Console.printLine("\t> Finished")
        yield result3

  
    override def run =
        common
            .tap(str => Console.printLine(str))
            .exitCode

