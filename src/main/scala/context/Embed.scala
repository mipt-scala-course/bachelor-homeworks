package context

import cats.FlatMap

trait Embed[U[_[_]]]:
  def embed[F[_] : FlatMap](a: F[U[F]]): U[F]

object Embed:
  def apply[U[_[_]] : Embed] = summon[Embed[U]]