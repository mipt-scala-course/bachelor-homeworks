package mipt.lecture7and8





object UnsafeDecl extends App:

    enum Exec[A]:
        case Cons[A](value: A) extends Exec[A]
        case Map[A, B](exec: Exec[A], f: A => B) extends Exec[B]
        case FlatMap[A, B](exec: Exec[A], f: A => Exec[B]) extends Exec[B]
    import Exec.*
    

    object Exec:
        def pure[A](a: A): Exec[A] = Cons(a)

    extension [A] (exec: Exec[A])
        def flatMap[B](f: A => Exec[B]): Exec[B] = FlatMap(exec, f)
        def map[B](f: A => B): Exec[B] = Map(exec, f)


    object Runtime:
        def run[A](exec: Exec[A]): A =
            exec match
                case Cons(value)      => value
                case Map(exec, f)     => f(run(exec))
                case FlatMap(exec, f) => run(f(run(exec)))
            

    // example

    def intDivision(dividend: Int, divisor: Int): Exec[Int] =
        for {
            div  <- Exec.pure(dividend)
            _     = println(s"divide $dividend by $divisor")
            res   = div / divisor
        } yield res

    val intDivOperation = intDivision(10, 0)

    println(Runtime.run(intDivOperation))
    println(Runtime.run(intDivOperation))






    




object SafeDecl extends App:
    import scala.util.{Try, Success, Failure}

    enum Exec[+A]:
        case Succeed[A](value: A) extends Exec[A]
        case Failed(error: Throwable) extends Exec[Nothing]
        case Map[A, B](exec: Exec[A], f: A => B) extends Exec[B]
        case FlatMap[A, B](exec: Exec[A], f: A => Exec[B]) extends Exec[B]
    import Exec.*

    object Exec:
        def pure[A](a: A): Exec[A] = Succeed(a)
        def raise(th: Throwable)   = Failed(th)

    extension [A] (exec: Exec[A])
        def flatMap[B](f: A => Exec[B]): Exec[B] = FlatMap(exec, f)
        def map[B](f: A => B): Exec[B] = Map(exec, f)


    object Runtime:
        def runSafe[A](exec: Exec[A]): Try[A] =
            exec match
                case Succeed(value)     => Success(value)
                case Failed(error)      => Failure(error)
                case Map(exec, f)       => runSafe(exec).map(f)
                case FlatMap(exec, f)   => runSafe(exec).flatMap(r => runSafe(f(r)))

    // example


    def intDivision(dividend: Int, divisor: Int): Exec[Int] =
        for {
            div  <- Exec.pure(dividend)
            _     = println(s"divide $dividend by $divisor")
            res   = div / divisor
        } yield res

    val intDivOperation = intDivision(10, 0)

    println(Runtime.runSafe(intDivOperation))
    println(Runtime.runSafe(intDivOperation))



