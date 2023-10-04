package mipt.applicatives

import mipt.applicatives.DoubleCalculator.calculator
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class ParserTest extends AnyFlatSpec with Matchers:
  behavior.of("DoubleCalculator")

  it should "parse positive int" in {
    calculator.parse("1349") shouldBe Right("", 1349)
  }

  it should "parse positive double with fraction part" in {
    calculator.parse("13.49") shouldBe Right("", 13.49)
  }

  it should "parse negative double" in {
    calculator.parse("-13.49") shouldBe Right("", -13.49)
  }

  it should "parse sum" in {
    calculator.parse("12.0+10.5") shouldBe Right("", 22.5)
  }

  it should "parse subtraction" in {
    calculator.parse("12.0-10.5") shouldBe Right("", 1.5)
  }

  it should "parse complex expression" in {
    calculator.parse("-7-3.0*4/6+10.5*2*2-2") shouldBe Right("", 31.0)
  }

  it should "parse only valid substring" in {
    calculator.parse("3*/24sdf") shouldBe Right("*/24sdf", 3)
  }
