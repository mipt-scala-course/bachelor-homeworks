package mipt.homework4

import mipt.utils.Homeworks._

object Task2 {

  /**
    * Релизовать ленивый бесконечный список, состоящий из степеней двойки
    * powersOfTwo = 2 #:: 4 #:: 8 #:: 16 #:: 32 ...
    */
  lazy val powersOfTwo: LazyList[BigInt] = task"Реализуйте метод powersOfTwo" (2, 1)

  /**
    * С помощью Решета Эратосфена реализовать ленивый бесконечный список, состоящий из простых чисел
    * sieveEratosthene = 2 #:: 3 #:: 5 #:: 7 #:: 11 #:: 13 #:: 17 ...
    *
    * В этой задаче не требуется оптимальный алгоритм, ожидается что хотя бы вычисление первой 1000 простых чисел
    * будет корректно работать.
    */
  lazy val sieveEratosthene: LazyList[Int] = task"Реализуйте метод sieveEratosthene" (2, 2)

  /**
    * Рассмотрим дерево, значения в узлах которого вычисляются лениво.
    */
  sealed trait Tree[+A]
  case object Leaf extends Tree[Nothing]
  class Node[A](v: => A, val l: Tree[A], val r: Tree[A]) extends Tree[A] {
    lazy val value: A = v

    override def equals(obj: Any): Boolean =
      obj match {
        case n: Node[A] => n.value == v && n.l == l && n.r == r
      }

    override def hashCode: Int = value.hashCode() * l.hashCode() * r.hashCode()
  }

  object Node {
    def apply[A](v: => A, l: Tree[A], r: Tree[A]) = new Node(v, l, r)
  }

  /**
    * Задача реализовать метод setMin, который не меняя структуру дерева, каждому узлу дерева
    * проставляет минимальное из значений всех узлов этого дерева.
    *
    * То есть:
    *             19                     2
    *            /  \                   / \
    *          24    2      ----->     2   2
    *         /  \                    / \
    *       84    42                 2   2
    *
    * Задача реализовать метод setMin в ОДИН проход дерева, используя рекурсивный lazy val
    * и вспомогательный метод `findMinAndSet(n: => Int, tree: Tree[Int]): (Option[Int], Tree[Int])`,
    * который возвращает пару: (минимальное значение узла в дереве, дерево где вместо значения подставлено n)
    *
    * Скорее всего, при реализации пригодится метод apply в объекте-компаньоне Node,
    * а метод toString будет полезен для отладки.
    *
    * Алгоритм может плохо справляться с большими деревьями, при реализоации НЕ обязательно использовать хвостовую рекурсию.
   **/
  def findMinAndSet(n: => Int, tree: Tree[Int]): (Option[Int], Tree[Int]) =
    task"Реализуйте метод findMinAndSet" (2, 3, 0)

  def setMin(tree: Tree[Int]): Tree[Int] = task"Реализуйте метод setMin" (2, 3)

}
