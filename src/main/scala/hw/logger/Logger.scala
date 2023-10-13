//> using dep org.typelevel::cats-core:2.10.0
package hw.logger

import cats.Monad
import cats.data.WriterT
import cats.syntax.applicative.*
import cats.implicits.catsSyntaxApplicativeId

case class Debug(debug: String)
case class Info(info: String)
case class Error(error: String)

type WriterTF[F[_], L] = [A] =>> WriterT[F, L, A]
case class LogEmbed[F[_], A](value: WriterT[WriterTF[WriterTF[F, Vector[Debug]], Vector[Info]], Vector[Error], A])

/**
 * I. Продвинутый логгер
 *
 * В этом задании вам предстоит реализовать логгер, имеющий 3 уровня записей - Debug, Info и Error.
 * Структура лога LogEmbed описана выше, от вас же требуется реализовать функции для удобного создания логов
 *
 */
final case class Logger[F[_]: Monad]():
  /**
   * I.1) Реализовать функцию debug
   *
   * Функция должна принимать сообщение уровня debug и возвращать лог, 
   * содержащий только это сообщение на правильном уровне
   *
   */
  def debug(debug: String): LogEmbed[F, Unit] = ???

  /**
   * I.1) Реализовать функцию info
   *
   * Функция аналогична предыдущей за исключением того, что уровень лога меняется на Info
   *
   */
  def info(info: String): LogEmbed[F, Unit] = ???

  /**
   * I.1) Реализовать функцию error
   *
   * Функция-аналог предыдущих двух для уровня Error
   *
   */
  def error(error: String): LogEmbed[F, Unit] = ???
