package mipt.homework3

import scala.annotation.tailrec

sealed trait MyList[+A] {

  def foldLeft[B](seed: B)(f: (B, A) => B): B = {
    @tailrec
    def innerFold(list: MyList[A], seed: B, f: (B, A) => B): B = list match {
      case MyCons(h, tail) => innerFold(tail, f(seed, h), f)
      case MyNil           => seed
    }

    innerFold(this, seed, f)
  }

  def reverse: MyList[A] =
    foldLeft(MyNil: MyList[A]) { (l, e) => MyCons(e, l) }

}

case class MyCons[A](head: A, tail: MyList[A]) extends MyList[A]
case object MyNil                              extends MyList[Nothing]
