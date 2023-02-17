package mipt.homework2

import org.scalacheck.{Arbitrary, Cogen, Gen}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.wordspec.AnyWordSpec
import org.scalatest.matchers.should._
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks

class FiniteEquivalence extends AnyWordSpec with Matchers with ScalaCheckPropertyChecks {
  import task2_finite_equivalences._
  import Ternary._

  implicit val arbitraryThree: Arbitrary[Ternary] = Arbitrary(Gen.oneOf(Yes, No, Maybe))
  implicit val cogenThree: Cogen[Ternary] = Cogen {
    case Yes   => 1L
    case No    => 2L
    case Maybe => 3L
  }

  "checking boolToThree" should {
    "be a section" in forAll { (f: Boolean => Ternary, b: Boolean) =>
      boolToThree.from(boolToThree.to(f))(b) shouldEqual f(b)
    }
    "be a retraction" in forAll { p: (Ternary, Ternary) => boolToThree.to(boolToThree.from(p)) shouldEqual p }
  }

  "checking threeToBool" should {
    "be a section" in forAll { (f: Ternary => Boolean, t: Ternary) =>
      threeToBool.from(threeToBool.to(f))(t) shouldEqual f(t)
    }
    "be a retraction" in forAll { t: (Boolean, Boolean, Boolean) => threeToBool.to(threeToBool.from(t)) shouldEqual t }
  }

  "checking boolToBoolToBool" should {
    "be a section" in forAll { (f: Boolean => Boolean => Boolean, x: Boolean, y: Boolean) =>
      boolToBoolToBool.from(boolToBoolToBool.to(f))(x)(y) shouldEqual f(x)(y)
    }
    "be a retraction" in forAll { (f: (Boolean => Boolean) => Boolean, g: Boolean => Boolean) =>
      boolToBoolToBool.to(boolToBoolToBool.from(f))(g) shouldEqual f(g)
    }
  }

  "checking AToUnit" should {
    "be a section" in forAll { (f: Ternary => Unit, t: Ternary) => AToUnit.from(AToUnit.to(f))(t) shouldEqual f(t) }
    "be a retraction" in forAll { u: Unit => AToUnit.to(AToUnit.from(u)) shouldEqual u }
  }

  "checking unitToA" should {
    "be a section" in forAll { (f: Unit => Ternary, u: Unit) => unitToA.from(unitToA.to(f))(u) shouldEqual f(u) }
    "be a retraction" in forAll { t: Ternary => unitToA.to(unitToA.from(t)) shouldEqual t }
  }

  "checking patternMatching" should {
    "be a section" in forAll { (f: Either[Int, Long] => Double, x: Either[Int, Long]) =>
      patternMatching.from(patternMatching.to(f))(x) shouldEqual f(x)
    }
    "be a retraction" in forAll { (f: Int => Double, g: Long => Double, x: Int, y: Long) =>
      val (f1, g1) = patternMatching.to(patternMatching.from((f, g)))
      f1(x) shouldEqual f(x)
      g1(x) shouldEqual g(x)
    }
  }

}
