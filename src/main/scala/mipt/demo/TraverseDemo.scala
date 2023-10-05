package mipt.demo

import cats.{Applicative, Functor, Monoid}
import cats.syntax.all.*

import scala.annotation.tailrec

object TraverseDemo:

  trait Traverse[F[_]] extends Functor[F]:
    def sequence[A, G[_]: Applicative](fa: F[G[A]]): G[F[A]]
    def traverse[G[_]: Applicative, A, B](fa: F[A])(f: A => G[B]): G[F[B]] =
      sequence(map(fa)(f))

  given Traverse[List] = new Traverse[List]:
    override def map[A, B](fa: List[A])(f: A => B): List[B] = fa.map(f)
    override def sequence[A, G[_]: Applicative](fa: List[G[A]]): G[List[A]] =
      swapList(fa, List.empty.pure[G])

    @tailrec
    private def swapList[A, G[_]: Applicative](list: List[G[A]], acc: G[List[A]]): G[List[A]] =
      list match
        case Nil    => acc
        case h :: t => swapList(t, (h, acc).mapN(_ :: _))

  object Const {
    opaque type C[A, B] = A

    def apply[A, B](x: A): Const[A, B] = x

    extension [A, B](c: C[A, B]) def get: A = c

    given [M: Monoid]: Applicative[C[M, *]] = new Applicative[C[M, *]]:
      def pure[A](x: A): M          = Monoid[M].empty
      def ap[A, B](ff: M)(fa: M): M = ff |+| fa

  }
  type Const[A, B] = Const.C[A, B]

  def foldMap[F[_]: Traverse, A, B: Monoid](fa: F[A])(f: A => B): B =
    summon[Traverse[F]].traverse[Const[B, *], A, B](fa)(x => Const[B, B](f(x))).get

@main def foldMapMain(): Unit =
  import TraverseDemo.*

  println(foldMap(List(1, 2, 3, 4))(_ * 2))
