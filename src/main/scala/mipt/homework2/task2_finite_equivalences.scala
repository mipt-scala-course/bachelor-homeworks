package mipt.homework2

import mipt.utils.Homeworks._

object task2_finite_equivalences {

  sealed trait Ternary
  object Ternary {
    case object Yes   extends Ternary
    case object No    extends Ternary
    case object Maybe extends Ternary
  }

  /**
    * Класс определяет эквивалентность типов
    * Для этого он получает функции прямого и обратного преобразований.
    * Необходимо передавать такие функции, что бы соблюдались правила:
    *
    * Для любого x, to(from(x)) = x
    * Для любого y, from(to(y)) = y
    *
    * @param to функция прямого преобразования
    * @param from функция обратного преобразования
    */
  class Equivalent[A, B](val to: A => B)(val from: B => A)
  object Equivalent {
    def apply[A, B](to: A => B)(from: B => A) =
      new Equivalent[A, B](to)(from)
  }

  /*
   * Решите следующие задания.
   * Вам необходимо реализовать функции прямого и обратного преобразований так,
   * что бы для инициализированного ими Equivalent выполнялись условия эквивалентности
   */

  task"определите эквивалентность экспоненты в булевый тип и произведения 3 ^ 2 = 3 * 3 "
  def boolToThree: Equivalent[Boolean => Ternary, (Ternary, Ternary)] = ???

  task"определите эквивалентность экспоненты в тернарный тип и произведения 2 ^ 3 = 2 * 2 * 2"
  def threeToBool: Equivalent[Ternary => Boolean, (Boolean, Boolean, Boolean)] = ???

  task"определите эквивалентность двух экпонент высшего порядка булевых типов (2 ^ 2) ^ 2 = 2 ^ (2 ^ 2)"
  def boolToBoolToBool: Equivalent[Boolean => Boolean => Boolean, (Boolean => Boolean) => Boolean] = ???

  task"докажите тривиальное качество терминального типа Unit 1 ^ A = 1"
  def AToUnit[A]: Equivalent[A => Unit, Unit] = ???

  task"докажите тривиальность экспоненты с единичным показателем A ^ 1 = A"
  def unitToA[A]: Equivalent[Unit => A, A] = ???

  task"докажите фундаментальное качество паттерн-матчинга C ^ (A + B) = C ^ A * C ^ B"
  def patternMatching[A, B, C]: Equivalent[(A Either B) => C, (A => C, B => C)] = ???

}
