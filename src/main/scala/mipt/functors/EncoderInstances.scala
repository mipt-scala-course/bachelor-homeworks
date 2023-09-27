package mipt.functors

import mipt.functors.domain.DegreesFahrenheit

trait OptionEncoderInstances:
  /** Реализуйте Encoder для Option и произвольного типа, для которого есть Encoder в скоупе. None должен
    * преобразовываться в значение `<none>`
    */
  given [T](using e: Encoder[T]): Encoder[Option[T]] = ???

trait ListEncoderInstances:
  /** Реализуйте Encoder для List и произвольного типа, для которого есть Encoder в скоупе. Элементы листа в
    * результирующей строке должны быть разделены запятой.
    */
  given [T: Encoder]: Encoder[List[T]] = ???

object EncoderInstances extends OptionEncoderInstances, ListEncoderInstances:
  import Encoder.given_Contravariant_Encoder
  import cats.implicits.toContravariantOps

  /** Реализуйте encoder для строки
    */
  given Encoder[String] = ???

  /** Реализуйте encoder числа в строку
    */
  given Encoder[Int] = ???

  /** Реализуйте encoder булева значения в строку
    */
  given Encoder[Boolean] = ???

  /** Реализуйте encoder для DegreesFahrenheit через использование существующего encoder и Contravariant
    */
  given Encoder[DegreesFahrenheit] = ???
