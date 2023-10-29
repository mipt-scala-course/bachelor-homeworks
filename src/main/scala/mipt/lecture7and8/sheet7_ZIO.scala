package mipt.lecture7and8

import zio.{Console, UIO, ZIO, ZIOAppDefault, ZLayer}
import zio.durationInt
import zio.Scope
import zio.ZIOAppArgs


object sheet7_ZIO extends ZIOAppDefault:

    val simpleExample =
        for
            value   <- ZIO.succeed(10)
            _       <- Console.printLine(s"value = $value") *> ZIO.sleep(1500.millis) <* Console.printLine("And now...")
            _       <- ZIO.fail(new Exception("Boom!"))
        yield ()


    val helloProgram: ZIO[Any, Exception, String] =
        for
            _       <- Console.printLine("Hello! What is your name?")
            name    <- Console.readLine
            _       <- Console.printLine("Really?").orDie
            confirm <- Console.readLine
            _ = throw new Exception()
            _       <- ZIO.when(!confirm.equalsIgnoreCase("yes"))(ZIO.fail(new Exception("Inadequate")))
        yield s"Hello, $name"



    override def run: ZIO[Environment & (ZIOAppArgs & Scope), Any, Any] =
        // simpleExample
        helloProgram
            .catchAll(e => Console.printLine(e))
            .flatMap(str => Console.printLine(str))
            // .catchAllDefect()







        

object AlterZioRun extends App:
    import zio.*

    val result = Unsafe.unsafe {
        implicit unsafe =>
            Runtime.default.unsafe.run( sheet7_ZIO.simpleExample )
    }




// ZIO Examlpes playground

object Playground extends ZIOAppDefault:

    // Services

    trait Generator[A]:
        def generate: A

    object Generator:
        def stringGenerator: Generator[String] =
            new:
                override def generate: String =
                    "1-2-3-4-5-6-7-8-9-0" 



    trait Modifier[A, B]:
        def modify(a: A): B

    object Modifier:
        def fromStringToIntFunction(f: String => Int): Modifier[String, Int] =
            new:
                override def modify(string: String): Int =
                    f(string) 



    trait Consumer[B]:
        def consume(b: B): ZIO[Any, Throwable, Unit]

    object Consumer:
        def buildIntConsumer: UIO[Consumer[Int]] =
            ZIO.succeed(
                new Consumer[Int]:
                    override def consume(value: Int): ZIO[Any, Throwable, Unit] =
                        Console.printLine(s"consumed $value chars")
            )



    val program: ZIO[Consumer[Int] & Modifier[String, Int] & Generator[String], Throwable, Unit] =
        for
            gen <- ZIO.service[Generator[String]]
            mod <- ZIO.service[Modifier[String, Int]]
            out <- ZIO.service[Consumer[Int]]
            _ <- out.consume(mod.modify(gen.generate))
        yield ()


    val layer1: ZLayer[Any, Nothing, Generator[String]] = ZLayer.succeed(Generator.stringGenerator)

    val layer2: ZLayer[String => Int, Nothing, Modifier[String, Int]] = ZLayer.fromFunction(Modifier.fromStringToIntFunction _)

    override def run =
        program
            .provide(
                ZLayer.succeed((string: String) => string.length),
                ZLayer.succeed(Generator.stringGenerator),
                ZLayer.fromFunction(Modifier.fromStringToIntFunction _),
                ZLayer.fromZIO(Consumer.buildIntConsumer),
                // ZLayer.Debug.mermaid
            )
            .exitCode

