package hw.traits

import cats.{Monad, Monoid}
import cats.data.{Writer, WriterT}
import cats.syntax.applicative.*

/**
 * III. Интерфейс Tell
 *
 * Данное задание очень похоже на предыдущее, только наш интерфейс будет не возвращать контекст, 
 * а, наоборот, отдавать дополнительную информацию во вне
 *
 */
trait Tell[F[_], W]:
  def tell(log: W): F[Unit]

object Tell:
  def apply[F[_], W](using tell: Tell[F, W]): Tell[F, W] = tell

  /**
   * III.1) Интерфейс Tell для Writer
   * 
   * Реализуйте интерфейс Tell так, чтобы полученное сообщение добавлялось в лог монады
   * 
   */
  given [W: Monoid]: Tell[Writer[W, *], W] = ???

  /**
   * III.2) Интерфейс Tell для WriterT
   *
   * Реализуйте интерфейс Tell так, чтобы полученное сообщение добавлялось в лог трансформера
   *
   */
  given [F[_]: Monad, W: Monoid]: Tell[WriterT[F, W, *], W] = ???
