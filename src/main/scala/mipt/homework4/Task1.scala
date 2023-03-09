package mipt.homework4

import mipt.utils.Homeworks._

import scala.annotation.tailrec

object Task1 {

  /**
    * MyStream - ленивый список, аналогичный LazyList в Scala
    */
  sealed trait MyStream[+A] {
    import MyStream._

    def take(n: Int): List[A] = takeInner(this, n, Nil).reverse

    def filter(predicate: A => Boolean): MyStream[A] =
      task"""
            Реализуйте метод фильтрации ленивого списка. Результатом тоже должен быть ленивый список
          """ (1, 1)

    def dropWhile(predicate: A => Boolean): MyStream[A] =
      task"""
            Реализуйте метод, который лениво отбросит все элементы, пока выполняется условие на элемент
          """ (1, 2)

    def flatMap[B](f: A => MyStream[B]): MyStream[B] =
      task"""
             Реализуйте метод flatMap, аналогичный такому у списка
             Метод должен работать лениво
          """ (1, 3)
  }

  case class NonEmptyStream[A] private (h: () => A, t: () => MyStream[A]) extends MyStream[A]
  case object Empty                                                       extends MyStream[Nothing]

  object MyStream {
    def apply[A](h: => A, t: => MyStream[A]): MyStream[A] =
      NonEmptyStream(() => h, () => t)

    @tailrec
    private def takeInner[A](stream: MyStream[A], n: Int, acc: List[A]): List[A] =
      stream match {
        case NonEmptyStream(h, t) if n > 0 => takeInner(t(), n - 1, h() :: acc)
        case _                             => acc
      }
  }
}
