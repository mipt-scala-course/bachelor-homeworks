package hw

import cats.Functor
import cats.data.NonEmptyList
import cats.Eval
import cats.syntax.traverse.*
import Tree.*

/**
 * I. Стэкобезопасная работа с бинарными деревьями
 *
 * Для реализации рекомендуется воспользоваться cats.Eval
 */
enum Tree[A]:
  case Leaf(a: A)
  case Node(value: A, to: NonEmptyList[Tree[A]])

object Tree:
  /**
   * I. 1. Реализуйте стэкобезопасный инстанс Functor
   *
   *           "one"
   *          /     \
   *       "who"     "shaves"
   *      /     \           \
   * "all"    "those"        "and"
   *        /    |    \        |  \
   *   "those" "only"  "who"  "do" "not"
   *      |
   *    "shave"
   *      |
   *  "themselves"
   *
   *             ||
   *             ||  .map(_.length)
   *             ||
   *             \/
   *
   *             3
   *          /     \
   *         3       6
   *      /     \     \
   *     3       5     3
   *           / | \   | \
   *          5  4  3  2  3
   *          |
   *          5
   *          |
   *         10
   *
   */
  given Functor[Tree] with
    def map[A, B](fa: Tree[A])(f: A => B): Tree[B] = ???

  /**
   * I. 2. Реализуйте свертку значений дерева, находящихся в листьях (Leaf), слева направо, используя бинарную операцию.
   *
   *             3
   *          /     \
   *         3       6
   *      /     \     \
   *     3       5     3
   *           / | \   | \
   *          5  4  3  2  3
   *          |
   *          5
   *          |
   *         10
   *
   *          ||
   *          || sumLeafs(_, op)
   *          ||
   *          \/
   *
   *  (((((3 op 10) op 4) op 3) op 2) op 3)
   *
   */
  def reduceLeftLeafs[A](tree: Tree[A], op: (A, A) => A): A = ???

//-----------------------------------------------------------------------------

/**
 * II. Свободные группы
 */

// Класс типов, определяющий группу над A
trait Group[A]:
  // ассоциативная операция
  def combine(x: A, y: A): A

  // нейтральный элемент: combine(empty, x) = x = combine(x, empty)
  def empty: A

  // обратный элемент:
  // combine(a, invert(a)) = empty = combine(invert(a), a)
  def invert(a: A): A

object Group:
  /**
   * II. 1. Реализуйте тип данных, соответствующий свободной группе
   */
  type Free[A] //= ???

  /**
   * II.2 Реализуйте инстанс группы для свободной группы
   */
  given [A]: Group[Free[A]] with
    def empty: Free[A] = ???
    def combine(x: Free[A], y: Free[A]): Free[A] = ???
    def invert(x: Free[A]): Free[A] = ???

  /**
   * II.3 Реализуйте функцию, поднимающую эелемент множества в элемент свободной группы
   */
  def lift[A](a: A): Free[A] = ???
