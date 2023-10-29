package mipt.lecture7and8



import cats.{Monad, MonadError}
import cats.data.EitherT
import cats.kernel.Semigroup

import scala.util.Try
import izumi.reflect.Tag






object sheet5_DI extends App:

    object Dirty:

        // Что можно сказать по подобной сигнатуре?
        // () => A

        trait Generator[A]:
            def generate: A

        val stringGenerator: Generator[String] =
            new:
                override def generate: String =
                    val start = s"[${java.time.Clock.systemUTC().millis()}]:"
                    Thread.sleep(scala.util.Random.nextInt(1250))
                    s"$start Generated value ${scala.util.Random.nextInt()} at ${java.time.Clock.systemUTC().millis()}"


    println("*"*16)
    println("Dirty:")
    // println(Dirty.stringGenerator.generate)
    // println(Dirty.stringGenerator.generate)
    // println(Dirty.stringGenerator.generate)
    println()







    object Failable:

        // Некоторый код, где задекларированы входные и выходные данные.
        // R => A

        trait Calculator[A: Semigroup]:
            def calculate(left: A, right: A): A =
                summon[Semigroup[A]].combine(left, right)


        given Semigroup[Int] with
            override def combine(x: Int, y: Int): Int = x / y


        val intCalculator = new Calculator[Int]{}


    println("*"*16)
    println("Failable:")
    // println(Failable.intCalculator.calculate(10, 0))
    // println(Failable.intCalculator.calculate(10, 1))
    // println(Failable.intCalculator.calculate(10, 2))
    println()
    








    object Complete:

        // Сигнатера обещает контроль над ошибками
        // R => F[E, A]

        trait Calculator[R, E, A]:
            def calculate(from: R): Either[E, A]


        val positiveCalculator: Calculator[Int, Exception, Double] =
            new:
                override def calculate(from: Int): Either[Exception, Double] =
                    from match
                        case 0          => Left(new Exception("Division by zero"))
                        case n if n < 0 => Left(new Exception("Negative"))
                        case positive   => Right( (1.0 / positive.toDouble)*100 )


    println("*"*16)
    // println("Complete:")
    // println(Complete.positiveCalculator.calculate(-5))
    // println(Complete.positiveCalculator.calculate( 0))
    // println(Complete.positiveCalculator.calculate( 5))
    println()






    // R => Either[E, A]
    // val result: Right | Left
    // 
    // R => Try[A]
    // val result: Success | Failure
    // 
    // R => Option[A]
    // val result: Some | None





    // Давайте в R вынесем взаимодействие с внешним миром








        

    object Dependant:

        object BusinessEntities:
            case class User(name: String)

        trait Program[R, E, A]:
            def doSomeWork(runtimeInteractions: R): Either[E, A]

        
        // Runtime features

        sealed trait Input[In]:
            def read: () => In

        sealed trait Output[Out]:
            def write: Out => Unit



        // Examples for program


        val authenticationService: Program[Input[String] with Output[String], Exception, BusinessEntities.User] =
            new:
                override def doSomeWork(runtimeInteractions: Input[String] & Output[String]): Either[Exception, BusinessEntities.User] =
                    import runtimeInteractions.*

                    write("Hello! What is your name?")
                    val name = read()

                    write("Really?")
                    val confirmation = read()

                    confirmation.toLowerCase() match
                        case "yes" =>
                            Right(BusinessEntities.User(name))
                        case _ =>
                            Left(new Exception("Inadequate"))
        
                end doSomeWork










        // Examples for runtime

        object Console extends Input[String] with Output[String]:
            override def read: () => String =
                scala.io.StdIn.readLine

            override def write: String => Unit =
                println



                


        object Mock extends Input[String] with Output[String]:
            import java.util.concurrent.atomic.AtomicReference
            private val inputs = new AtomicReference[List[String]]("Alice Cooper" :: "yes" :: "Pretty kitten" :: "meow!" :: Nil)

            override def read: () => String =
                () => inputs.get() match
                    case head :: tail =>
                        inputs.set(tail ::: head :: Nil)
                        head
                    case _ =>
                        ""
                

            override def write: String => Unit =
                _ => ()



        trait MockedStringInput extends Input[String]:
            import java.util.concurrent.atomic.AtomicReference
            private val inputs = new AtomicReference[List[String]]("Alice Cooper" :: "yes" :: "Pretty kitten" :: "meow!" :: Nil)

            override def read: () => String =
                () => inputs.get() match
                    case head :: tail =>
                        inputs.set(tail ::: head :: Nil)
                        head
                    case _ =>
                        ""

        trait ConsoleStringOutput extends Output[String]:
            override def write: String => Unit =
                println



    println("*"*16)
    println("Dependant:")
    val depCalculation = Dependant.authenticationService
    // println(depCalculation.doSomeWork(Dependant.Mock))
    // println(depCalculation.doSomeWork(Dependant.Mock))
    // println(depCalculation.doSomeWork(Dependant.Console))
    // println(depCalculation.doSomeWork(new Dependant.MockedStringInput with Dependant.ConsoleStringOutput {}))
    println()        









    // Теперь можно попробовать реализовать систему эффектов с контролем зависимостей и ошибок



    object Effects:

        // Effects:

        enum Effect[-R, +E, +A]:
            case Success[A](a: A) extends Effect[Any, Nothing, A]
            case Failure[E](e: E) extends Effect[Any, E, Nothing]
            case Function[R, A](f: R => A) extends Effect[R, Nothing, A]
            case Map[R, E, A, B](effect: Effect[R, E, A], f: A => B) extends Effect[R, E, B]
            case FlatMap[R0, R1, E, A, B](effect: Effect[R0, E, A], f: A => Effect[R1, E, B]) extends Effect[R0 with R1, E, B]
        import Effect.*


        object Effect:
            def succeed[A](a: A): Effect[Any, Nothing, A] =
                Success(a)
            def failed[E](e: E): Effect[Any, E, Nothing] =
                Failure(e)
            def fromFunction[R, A](f: R => A): Effect[R, Nothing, A] =
                Function(f)

        extension [R, E, A] (effect: Effect[R, E, A])

            def map[B](f: A => B): Effect[R, E, B] =
                Map(effect, f)

            def flatMap[R0, B](f: A => Effect[R0, E, B]) =
                FlatMap(effect, f)



        // Business entities

        object BusinessEntities:
            case class User(name: String)


        
        // Runtime features

        sealed trait Input[In]:
            def read: () => In

        sealed trait Output[Out]:
            def write: Out => Unit

        object Console extends Input[String] with Output[String]:
            override def read: () => String =
                scala.io.StdIn.readLine

            override def write: String => Unit =
                println




        // Examples for program
        val program: Effect[Input[String] with Output[String], Throwable, BusinessEntities.User] =
            for
                reader <- Effect.fromFunction( (cons: Input[String])  => cons.read  )
                writer <- Effect.fromFunction( (cons: Output[String]) => cons.write )
                _       = writer("Hello! What is your name?")
                name    = reader()
                _       = writer("Really?")
                conf    = reader()
                result <- 
                    if (conf.compareToIgnoreCase("yes") != 0)
                        Effect.succeed(BusinessEntities.User(name))
                    else 
                        Effect.failed(new Exception("Inadequate"))
            yield result




        // example runtime

        trait Runtime:
            def run[R, E, A](context: R)(effect: Effect[R, E, A]): Either[E, A]
            def runUnsafe[R, E, A](context: R)(effect: Effect[R, E, A]): A =
                run(context)(effect).toOption.get

        def defaultRuntime: Runtime =
            new:
                override def run[R, E, A](context: R)(effect: Effect[R, E, A]): Either[E, A] =
                    // Это демонстрация идеи, работать не будет
                    effect match
                        case Success(value)     => Right(value)
                        case Failure(error)     => Left(error)
                        case Function(f)        => scala.util.Try(f(context)).toEither.asInstanceOf // никогда так не делайте
                        case Map(effect, f)     => run(context)(effect).flatMap(r => scala.util.Try(f(r)).toEither.asInstanceOf)
                        case FlatMap(effect, f) => run(context)(effect).flatMap(r => scala.util.Try(run(context)(f(r))).toEither.asInstanceOf)


        val result1: Either[Throwable, BusinessEntities.User] = defaultRuntime.run(Console)(program)
        val result2: BusinessEntities.User                    = defaultRuntime.runUnsafe(Console)(program)

    end Effects




