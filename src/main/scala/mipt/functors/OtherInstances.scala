package mipt.functors

import cats.Functor

trait OtherInstances:
  type Arr[-C, +A] = C => (List[A] => C) => A

  /** Реализуйте инстанс Functor для Arr[C, *] (по второму аргументу)
    */
  given [C]: Functor[Arr[C, *]] = ???

  /** Реализуйте инстанс для композиции функторов и докажите законы функтора для этого инстанса
    */
  given [F[_]: Functor, G[_]: Functor]: Functor[[x] =>> F[G[x]]] =
    new Functor[[x] =>> F[G[x]]]:
      override def map[A, B](fga: F[G[A]])(f: A => B): F[G[B]] =
        val fFunctor = summon[Functor[F]]
        val gFunctor = summon[Functor[G]]
        fFunctor.map(fga) { ga =>
          gFunctor.map(ga)(f)
        }
