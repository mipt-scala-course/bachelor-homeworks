package mipt.lecture7and8

import scala.util.{Try, Success, Failure}







object Unsafe extends App:

    // абстракция для функционального эффекта
    case class Exec[A](run: () => A)

    object Exec:
        def pure[A](a: => A): Exec[A] =
            Exec(() => a)

    extension [A] (exec: Exec[A])

        def flatMap[B](f: A => Exec[B]): Exec[B] =
            Exec(() => f(exec.run()).run())

        def map[B](f: A => B): Exec[B] =
            Exec(() => f(exec.run()))

        def foreach(f: A => Unit): Exec[Unit] =
            Exec(() => f(exec.run()))


    // example

    val input               = Exec.pure(scala.io.StdIn.readLine())
    def output(str: String) = Exec.pure(println(str)) 

    def intDivision(dividend: Int, divisor: Int) =
        for {
            _    <- output("Give me something here:")
            str  <- input
            _    <- output(s"> $str")
            div  <- Exec.pure(dividend)
            res   = div / divisor
            _    <- output(s"divided $dividend by $divisor")
        } yield res

    val intDivOperation = intDivision(10, 0)

    println(Try(intDivOperation.run()))
    println(Try(intDivOperation.run()))






object SafeWithOption extends App:

    // Эффект, контролирующий ошибки
    case class Exec[A](runSafe: () => Option[A]) {
        def runUnsafe(): A = runSafe().get
    }

    object Exec:
        def pure[A](a: A): Exec[A] =
            Exec(() => Some(a))
        def raise[A]: Exec[A] =
            Exec(() => None)

    extension [A] (exec: Exec[A])

        def map[B](f: A => B): Exec[B] =
            Exec( () =>
                exec.runSafe() match
                    case Some(value) =>
                        Try(f(value)).toOption
                    case fail =>
                        fail.asInstanceOf[Option[B]]
            )

        def flatMap[B](f: A => Exec[B]): Exec[B] =
            Exec( () => 
                exec.runSafe() match
                    case Some(value) =>
                        f(value).runSafe()
                    case fail =>
                        fail.asInstanceOf[Option[B]]
            )

    // example 
    
    def intDivision(dividend: Int, divisor: Int): Exec[Int] =
        for {
            div  <- Exec.pure(dividend)
            _     = println(s"divide $dividend by $divisor")
            res   = div / divisor
        } yield res

    val intDivOperation = intDivision(10, 0)

    println(intDivOperation.runSafe())    
    println(intDivOperation.runUnsafe())






    

object SafeWithTry extends App:
    
    // Контроллируем и сохраняем ошибки
    case class Exec[A](runSafe: () => Try[A]) {
        def run(): A = runSafe().get
    }

    object Exec:
        def pure[A](a: A): Exec[A] =
            Exec(() => Success(a))
        def raise[A](th: Throwable): Exec[A] =
            Exec(() => Failure(th))

    extension [A] (exec: Exec[A])

        def map[B](f: A => B): Exec[B] =
            Exec( () =>
                exec.runSafe() match
                    case Success(value) =>
                        Try(f(value))
                    case fail =>
                        fail.asInstanceOf[Try[B]]
            )

        def flatMap[B](f: A => Exec[B]): Exec[B] =
            Exec( () => 
                exec.runSafe() match
                    case Success(value) =>
                        f(value).runSafe()
                    case fail =>
                        fail.asInstanceOf[Try[B]]
            )
    
    // example

    def intDivision(dividend: Int, divisor: Int): Exec[Int] =
        for {
            div  <- Exec.pure(dividend)
            _     = println(s"divide $dividend by $divisor")
            res   = div / divisor
        } yield res

    val intDivOperation = intDivision(10, 0)

    println(intDivOperation.runSafe())    
    println(intDivOperation.run())





