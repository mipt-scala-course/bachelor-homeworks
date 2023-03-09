package mipt.homework4

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import Task3._
import TestStx._

class Task3Spec extends AnyFlatSpec with Matchers {
  "toInt and fromInt" should "convert int values" in {
    0.n.toInt should equal(0)
    100.n.toInt should equal(100)
    100.n.toInt should equal(100)
  }

  "===" should "check equality of natural numbers" in {
    (1.n === 1.n) should equal(true)
    (0.n === 0.n) should equal(true)
    (42.n === 42.n) should equal(true)
  }

  "+" should "sum natural numbers" in {
    (10.n + 2.n) should equal(12.n)
    (5.n + 3.n) should equal(8.n)
    (0.n + 0.n) should equal(0.n)
  }

  "-" should "subtract natural numbers" in {
    (5.n - 3.n) should equal(Some(2.n))
    (5.n - 6.n) should equal(None)
    (30.n - 15.n) should equal(Some(15.n))
  }

  "*" should "multiply natural numbers" in {
    (5.n * 6.n) should equal(30.n)
    (12.n * 0.n) should equal(0.n)
    (1.n * 42.n) should equal(42.n)
  }

  "/" should "divide natural numbers" in {
    (5.n / 6.n) should equal(Some(0.n))
    (6.n / 6.n) should equal(Some(1.n))
    (19.n / 5.n) should equal(Some(3.n))
    (42.n / 0.n) should equal(None)
    (0.n / 0.n) should equal(None)
  }

  "pow" should "exponent natural numbers" in {
    (2.n pow 2.n) should equal(4.n)
    (42.n pow 0.n) should equal(1.n)
    (0.n pow 1.n) should equal(0.n)
    (1.n pow 30.n) should equal(1.n)
    (3.n pow 2.n) should equal(9.n)
    (0.n pow 0.n) should equal(1.n)
  }

  "complex expressions" should "be evaluated correctly" in {
    (((1.n + 3.n) pow 2.n) - 10.n) should equal(13.n / 2.n)
  }
}

object TestStx {
  implicit class IntTestStx(val i: Int) extends AnyVal {
    def n: Num            = nOpt.get
    def nOpt: Option[Num] = Num.fromInt(i)
  }
}
