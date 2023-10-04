package mipt.applicatives

import cats.Alternative
import cats.syntax.all.*

object Parser:
  opaque type P[A] = String => Either[String, (String, A)]

  extension [A](parser: P[A]) def parse(input: String): Either[String, (String, A)] = parser(input)

  def defer[A](p: => P[A]): P[A] = p(_)

  def charWhere(f: Char => Boolean): P[Char] = str =>
    str.headOption.toRight("expected at least one more char symbol").flatMap {
      case x if f(x) => Right((str.tail, x))
      case x         => Left(s"unexpected symbol \"$x\"")
    }

type Parser[A] = Parser.P[A]

import Parser.*

object DoubleCalculator:

  /** Реализуйте парсер для калькулятора вещественных чисел, который разбирает строку и считает значение выражения
    * Выражения могут содержать:
    *   - вещественные числа, в том числе отрицательные (задаются как с точкой и дробной частью, так и без)
    *   - умножение произвольного количества вещественных чисел
    *   - деление произведения или вещественного числа на число
    *   - сумму и разность произвольного количества аргументов Не требуется реализовывать скобки, деление на
    *     произведение, несколько делений подряд
    * Примеры валидных выражений
    *   - 1+1
    *   - -10+20.5
    *   - 19*10*2/5-28+42
    */
  def calculator: Parser[Double] = ???
