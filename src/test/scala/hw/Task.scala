package hw

import java.time.LocalDate
import scala.jdk.CollectionConverters.*
import scala.util.Random
import cats.syntax.functor.*
import cats.syntax.traverse.*
import cats.Eval
import cats.data.NonEmptyList

import Tree.*

class Tests extends munit.FunSuite:
  val testAttempts = 10

  def genListTree[A, B](genA: => A, f: A => B, size: Int): (Tree[A], Tree[B]) =
    (1 to size).foldLeft {
      val init = genA
      (Leaf(init), Leaf(f(init)))
    } {
      case ((accA, accB), _) =>
        val next = genA
        (Node(next, NonEmptyList.one(accA)), Node(f(next), NonEmptyList.one(accB)))
    }

  def equal[A](x: Tree[A], y: Tree[A]): Eval[Option[String]] =
    (x, y) match
      case (Leaf(l), Leaf(r)) =>
        Eval.now(
          Option.when(l != r)(s"([L] $l != $r)")
        )
      case (Node(l, tsL), Node(r, tsR)) =>
        if (l != r) Eval.now(Some(s"([N] $l != $r)"))
        else if (tsL.length != tsR.length) Eval.now(Some(s"([N] value $l, different shapes: ${tsL.length} != ${tsR.length})"))
        else Eval.defer(
          tsL.zip(tsR).traverse(
            (tL, tR) =>
              equal(tL, tR)
          ).map { res =>
            val errs = res.toList.flatten
            errs.headOption.map(_ => errs.mkString)
          }
        )
      case (_, _) => Eval.now(Some("different shapes"))

  test("I. 1. map for tree is stack-friendly"):
    val f = (x: Int) => x % 7
    val (t1, t2) = genListTree(Random.between(1, 100), f, 500000)
    assertEquals(
      equal(t1.map(f), t2).value,
      None
    )

  def genOneFatNodeTree[A](genA: => A, f: (A, A) => A, size: Int, maxEachSize: Int): (Tree[A], A) =
    val init = genA
    val (ts, res) = (1 to size).foldLeft(
      (NonEmptyList.one[Tree[A]](Leaf(init)), init)
    ) { case ((acc, i), _) =>
      val next = genA
      (acc.append(genListTree(next, identity, maxEachSize)._1), f(i, next))
    }

    (Node(genA, ts), res)

  test("I. 2. reduce left leaf for tree is stack-friendly"):
    val f = (x: Int, y: Int) => (x - y) % 19
    val (tree, res) = genOneFatNodeTree(Random.between(1, 100), f, 10000, 100)

    assertEquals(reduceLeftLeafs(tree, f), res)

  type T = Group.Free[Int]
  val g: Group[T] = summon

  val zero = g.empty

  def gen(): T =
    val size = Random.between(1, 100)
    (1 to size).foldLeft(Group.lift(1)) {
      case (acc, _) =>
        val add = Group.lift(Random.between(1, 10))
        val addInv =
          if (Random.nextBoolean) add
          else g.invert(add)

        g.combine(acc, addInv)
    }

  test("II. Free group laws: empty element is empty"):
    (1 to testAttempts).foreach { _ =>
      val x = gen()
      assertEquals(g.combine(x, zero), x)
      assertEquals(g.combine(zero, x), x)
    }

  test("II. Free group laws: combine is associative"):
    (1 to testAttempts).foreach { _ =>
      val x = gen()
      val y = gen()
      val z = gen()

      assertEquals(g.combine(g.combine(x, y), z), g.combine(x, g.combine(y, z)))
    }

  test("II. Free group laws: invert operation is lawful"):
    (1 to testAttempts).foreach { _ =>
      val x = gen()

      assertEquals(g.combine(g.invert(x), x), zero)
      assertEquals(g.combine(x, g.invert(x)), zero)
    }

