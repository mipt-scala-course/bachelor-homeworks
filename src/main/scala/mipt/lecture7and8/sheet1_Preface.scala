package mipt.lecture7and8

import cats.kernel.Monoid


object Adt:

    
    // Что такое ADT
    enum Computation[A]:
        case Cons[A](value: A) extends Computation[A]
        case Combine[A](first: Computation[A], second: Computation[A]) extends Computation[A]
    
    import Computation.*

    
    // Как ADT можно применить.
    // Декларативная модель.
    extension [A] (calc: Computation[A])
        def combineWith(other: Computation[A]): Computation[A] =
            Combine(calc, other)

    val example: Computation[Int] = // 1 + 1 + 1 + 1
        Cons(1).combineWith(Cons(1)).combineWith(Cons(1)).combineWith(Cons(1))

    def exec(calculation: Computation[Int]): Int =
        calculation match
            case Cons(value) => value
            case Combine(first, second) => exec(first) + exec(second)

    val result = exec(example)






object Executional:

    // Исполняемая модель
    case class Exec[A](run: () => A)

    def Pure[A](value: => A): Exec[A] =
        Exec(() => value)

    extension [A: Monoid] (exec: Exec[A])
        def combineWith(other: Exec[A]): Exec[A] =
            Exec(() => summon[Monoid[A]].combine(exec.run(), other.run()))

    val example: Exec[Int] = // 1 + 1 + 1 + 1
        Pure(1).combineWith(Pure(1)).combineWith(Pure(1)).combineWith(Pure(1))

    val result = example.run()





@main
def run(): Unit =
    println(Adt.result)
    println(Executional.result)

    