package mipt.lecture7and8

import zio.*
import zio.logging.*

import java.io.IOException


object sheet9_Fiber extends ZIOAppDefault:
    override val bootstrap = Runtime.removeDefaultLoggers >>> console(LogFormat.colored)


    // Представим, что у нас есть три тяжёлых вычисления

    val heavyMetalCalculation: ZIO[Any, IOException, String] =
        for {
            _ <- ZIO.logInfo("]-> starting")
            _ <- ZIO.sleep(Duration.fromMillis(1800))
            _ <- ZIO.logInfo("]-> finishing")
        } yield "Warlord returns"


    val hardRockWork =
        for {
            _ <- ZIO.logWarning("*-> starting")
            _ <- ZIO.sleep(Duration.fromMillis(500))
            _ <- ZIO.logWarning("*-> finishing")
        } yield "Crazy train"


    val punkIsNotDead =
        for {
            _ <- ZIO.logError(">-> starting")
            _ <- ZIO.sleep(Duration.fromMillis(3000))
            _ <- ZIO.logError(">-> finishing")
        } yield "Offspring" -> "Smash"


    // Собираем их вместе

    val common1 =
        for {
            _ <- Console.printLine("Starting")

            // fiberMetal: Fiber[IOException, Int] <- heavyMetalCalculation.fork
            fiberMetal <- heavyMetalCalculation.fork
            fiberRocky <- hardRockWork.repeatN(3).fork
            fiberPunk  <- punkIsNotDead.fork

            _ <- Console.printLine("Waiting")

            metal <- fiberMetal.join
            rocky <- fiberRocky.await

            punkStatus  <- fiberPunk.status
            _           <- Console.printLine(s"Punk is $punkStatus")
            _           <- ZIO.sleep(Duration.fromMillis(100))
            punks       <- fiberPunk.interrupt

            _ <- Console.printLine("Completing")
        } yield s"$metal with $rocky and $punks"


    // override def run: ZIO[Any, Any, Any] =
    //     common1
    //         .tap(result => Console.printLine(result))


    // И ещё


    val common2 =
        for {
            _ <- Console.printLine("Starting")

            // fiberMetal: Fiber[IOException, Int] <- heavyMetalCalculation.fork
            fiber1 <- heavyMetalCalculation.fork
            fiber2 <- hardRockWork.repeatN(3).fork
            fiber3 <- punkIsNotDead.forkDaemon

            _ <- Console.printLine("Waiting")

            zips   = fiber1.zip(fiber2)
            fibers = zips.orElse(fiber3)


            _ <- ZIO.sleep(Duration.fromMillis(1000))
            // _ <- fiber3.interrupt
            result <- fibers.join

            _ <- Console.printLine("Completing")
        } yield result



    // override def run: ZIO[Any, Any, Any] =
    //     common2
    //         .tap(result => Console.printLine(s"${result._1} with ${result._2}"))



    

    //





    def heavyMetalSync(sync: Promise[Nothing, (String, String)]): ZIO[Any, IOException, String] =
        for {
            _ <- ZIO.logInfo("]-> starting")
            _ <- ZIO.sleep(Duration.fromMillis(1800))
            _ <- ZIO.logInfo("]-> finishing")
            _ <- sync.succeed("Warlord returns" -> "Warriors of the World")
        } yield "Manovar"

    def hardRockSync(sync: Promise[Nothing, (String, String)]): ZIO[Any, IOException, String] =
        for {
            _ <- ZIO.logWarning("*-> starting")
            _ <- ZIO.sleep(Duration.fromMillis(1500))
            _ <- ZIO.logWarning("*-> finishing")
            _ <- sync.succeed("Blizzard of Ozz" -> "Crazy train")
        } yield "Ozzy Osbourne"

    def punkRockSync(sync: Promise[Nothing, (String, String)]): ZIO[Any, IOException, String] =
        for {
            _ <- ZIO.logError(">-> starting")
            _ <- ZIO.sleep(Duration.fromMillis(2000))
            _ <- ZIO.logError(">-> finishing")
            _ <- sync.succeed("Smash" -> "Gotta get away")
        } yield "Offspring"

    val common3 =
        ZIO.scoped:
            for 
                _ <- Console.printLine("\t> Starting")

                sync <- Promise.make[Nothing, (String, String)]

                hub <- Hub.bounded(10)

                fiber1 <- heavyMetalSync(sync).repeatN(1).fork
                fiber2 <- hardRockSync(sync).repeatN(3).forkScoped
                fiber3 <- punkRockSync(sync).repeatN(2).forkDaemon

                _ <- Console.printLine("\t> Waiting")

                result <- sync.await

                _ <- Console.printLine("\t> Finished")
            yield result


    override def run: ZIO[Any, Any, Any] =
        common3
            .tap(_ => ZIO.sleep(Duration.fromSeconds(6)))
            .flatMap(result => Console.printLine(s"'${result._1}'' with '${result._2}'"))



