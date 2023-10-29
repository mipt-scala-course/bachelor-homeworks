package mipt.lecture7and8


import cats.effect.{ExitCode, IO, IOApp}

import scala.concurrent.duration._
import cats.Monad




object sheet4_CatsEffect extends IOApp.Simple:

    val simpleExample =
        for
            value   <- IO.pure(10)
            _       <- IO.println(s"value = $value") >> IO.sleep(1500.millis) >> IO.println("And now...")
            _       <- IO.raiseError(new Exception("Boom!"))
        yield ()


    val helloProgram =
        for
            _       <- IO.println("Hello! What is your name?")
            name    <- IO.readLine
            _       <- IO.println("Really?")
            confirm <- IO.readLine
            _       <- IO.raiseWhen(!confirm.equalsIgnoreCase("yes"))(new Exception("Inadequate"))
            _       <- IO.println(s"Hello, $name!")
        yield s"Hello, $name"


    val monad: Monad[IO] = Monad[IO]

    // monad.tailRecM()


    override val run =
        // simpleExample
        (helloProgram flatMap IO.println)
            .handleErrorWith(IO.println)










object AlterCeRun extends App:
    import cats.effect.unsafe.implicits._


    val simpleExample =
        for
            value   <- IO.pure(10)
            _       <- IO.println(s"value = $value") >> IO.sleep(1500.millis) >> IO.println("And now...")
           // _       <- IO.raiseError(new Exception("Boom!"))
        yield ()


    simpleExample.unsafeRunSync()
    simpleExample.unsafeRunSync()


    // Cats effect playground
