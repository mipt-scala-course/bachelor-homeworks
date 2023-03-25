package mipt.homework5

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TaskSpec extends AnyFlatSpec with Matchers {

  "-" should "subtract polynoms for real numbers and boolean field" in {
    Polynom.from[BigDecimal](3, 2, 1) minus Polynom.from(2, 1, 0) should be(
      Polynom.from[BigDecimal](1, 1, 1)
    )

    Polynom.from[Double](4, 1) minus Polynom.from(6, -1.5, 0.1, 2) should be(
      Polynom.from[Double](-6, 1.5, 3.9, -1)
    )

    Polynom.from(true) minus Polynom.from(true, true, true, true) should be(
      Polynom.from(true, true, true, false)
    )
  }

  "-" should "subtract polynoms for finite primary field" in {
    implicit val pfield: Field[Int] = Field.pField(7)

    Polynom.from(3, 2, 1) minus Polynom.from(2, 1, 0) should be(
      Polynom.from(1, 1, 1)
    )

    Polynom.from(1, 1, 0) minus Polynom.from(2, 1, 0) should be(
      Polynom.from(6, 0, 0)
    )

    Polynom.from(3, 2, 3, 2) minus Polynom.from(6, 5, 1) should be(
      Polynom.from(3, 3, 5, 1)
    )
  }

  "gcd" should "eval gcd for polynoms on fractional field" in {
    Polynom
      .from[BigDecimal](1, -1, 2, 8, -10)
      .gcd(
        Polynom.from(1, 0, 3, 10, 2, 10)
      ) should be(Polynom.from(1, 0, 2, 10))
  }

  "gcd" should "eval gcd for polynoms on finite field prime sized" in {
    implicit val pfield: Field[Int] = Field.pField(7)

    Polynom
      .from[Int](1, 6, 2, 1, 4)
      .gcd(
        Polynom.from(1, 0, 3, 3, 2, 3)
      ) should be(Polynom.from(1, 0, 2, 3))
  }

  "linearCombination" should "calculate linear combination for field vector space" in {
    import Field._
    import VectorSpace.fieldInstance

    VectorSpace.linearCombination(
      Vector(
        (10.0, 10.0),
        (10.0, 20.0),
        (15.0, 10.0)
      )
    ) shouldBe 450.0

  }

  "linearCombination" should "calculate linear combination for pair vector space" in {
    import Field._
    import VectorSpace.pairInstance

    VectorSpace.linearCombination(
      Vector(
        ((10.0, 10.0), 2.0),
        ((10.0, 20.0), 2.0),
        ((15.0, 10.0), 3.0)
      )
    ) shouldBe (85.0, 90.0)

  }

  "linearCombination" should "calculate linear combination for functional vector space" in {
    import Field._
    import VectorSpace.funcInstance

    val f = VectorSpace.linearCombination(
      Vector(
        ((a: Double) => a * 2.0, 1.0),
        ((a: Double) => a * 3 + 5, 2.0),
        ((a: Double) => a + 10, 5.0)
      )
    )

    f(10.0) shouldBe 190.0
    f(20.0) shouldBe 320.0
  }

}
