package mipt.functors

import cats.Contravariant

trait Encoder[-T]:
  def apply(value: T): String

object Encoder:
  def encode[T](value: T)(using encoder: Encoder[T]): String =
    encoder(value)

  given Contravariant[Encoder] = new Contravariant[Encoder]:
    /** Реализуйте Contravariant для Encoder
      */
    override def contramap[A, B](fa: Encoder[A])(f: B => A): Encoder[B] = ???
