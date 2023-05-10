package mipt.homework11

import eu.timepit.refined.api.Validate
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Random

class Homework11Spec extends AnyFunSpec with Matchers {
  import refined._

  describe("Rank") {
    def test(idx: Int, num: Int, isRank: Boolean): Unit =
      it(s"$num is ${if (!isRank) "not " else ""}a rank; $idx") {
        Rank(num) should matchPattern {
          case Right(_: Rank) if isRank =>
          case Left(_) if !isRank       =>
        }
      }

    0 to 100 foreach (test(0, _, isRank = true))

    Iterator
      .continually(Random.nextInt())
      .filterNot(it => it >= 0 && it <= 100)
      .take(100)
      .zipWithIndex
      .foreach { case (num, idx) => test(idx, num, isRank = false) }

  }

  describe("OptionalTrue") {
    def test(boolOpt: Option[Boolean], isOptionalTrue: Boolean): Unit =
      it(s"$boolOpt is ${if (!isOptionalTrue) "not " else ""}an optional true") {
        OptionalTrue(boolOpt) should matchPattern {
          case Right(_: OptionalTrue) if isOptionalTrue =>
          case Left(_) if !isOptionalTrue               =>
        }
      }

    test(Some(true), isOptionalTrue = true)
    test(Some(false), isOptionalTrue = false)
    test(None, isOptionalTrue = true)
  }

  describe("NonEmptyList") {
    def test[A](list: List[A], isNonEmpty: Boolean): Unit =
      it(s"$list is ${if (isNonEmpty) "not " else ""}empty") {
        NonEmptyList(list) should matchPattern {
          case Right(_: NonEmptyList[_]) if isNonEmpty =>
          case Left(_) if !isNonEmpty                  =>
        }
      }

    test(List.empty, isNonEmpty = false)
    test(List(1, 2, 3), isNonEmpty = true)
    test(List(Complex(2, 3)), isNonEmpty = true)
  }

  describe("SetWithZero") {
    def test[A: Numeric](set: Set[A], contains0: Boolean)(
        implicit validate: Validate[Set[A], SetWithZero.Valid]
    ): Unit =
      it(s"$set ${if (!contains0) "not " else ""} contains 0") {
        SetWithZero(set) should matchPattern {
          case Right(_: SetWithZero[_]) if contains0 =>
          case Left(_) if !contains0                 =>
        }
      }

    test(Set(1, 2, 3), contains0 = false)
    test(Set.empty[Double], contains0 = false)
    test(Set(1L, 10L, 0L), contains0 = true)
    test(Set(BigInt("123"), BigInt("12312412415125123413"), BigInt("0")), contains0 = true)
  }

  describe("IpAddress") {
    def test(string: String, isIpAddress: Boolean): Unit =
      it(s"$string is ${if (isIpAddress) "" else "not "}an ip address") {
        IpAddress(string) should matchPattern {
          case Right(_: IpAddress) if isIpAddress =>
          case Left(_) if !isIpAddress            =>
        }
      }

    test("1.1.1.1", isIpAddress = true)
    test("0.0.0.0", isIpAddress = true)
    test("lsakfmaskd", isIpAddress = false)
    test("-1.1.255.0", isIpAddress = false)
    test("255.255.255.256", isIpAddress = false)
    test("255.255.255.255", isIpAddress = true)

    test("2001:0db8:85a3:0000:0000:8a2e:0370:7334", isIpAddress = true)
  }

  describe("RussianMobilePhone") {
    def test(string: String, isRussianMobilePhone: Boolean): Unit =
      it(s"$string is ${if (isRussianMobilePhone) "" else "not "}a russian mobile phone") {
        RussianMobilePhone(string) should matchPattern {
          case Right(_: RussianMobilePhone) if isRussianMobilePhone =>
          case Left(_) if !isRussianMobilePhone                     =>
        }
      }

    test("asldmasd", isRussianMobilePhone = false)
    test("+77078062222", isRussianMobilePhone = false)
    test("123456789", isRussianMobilePhone = false)
    test("+79160045555", isRussianMobilePhone = true)
  }

  describe("Zero") {
    def test(idx: Int, complex: Complex, isZero: Boolean): Unit =
      it(s"$complex is ${if (isZero) "" else "not "}a zero; $idx") {
        Zero(complex) should matchPattern {
          case Right(_: Zero) if isZero =>
          case Left(_) if !isZero       =>
        }
      }

    test(0, Complex(0, 0), isZero = true)

    Iterator
      .continually((Random.nextDouble(), Random.nextDouble()))
      .collect { case (r, i) if !(r == 0 && i == 0) => Complex(r, i) }
      .take(100)
      .zipWithIndex
      .foreach { case (num, idx) => test(idx, num, isZero = false) }
  }

  describe("Real") {
    def test(complex: Complex, isReal: Boolean): Unit =
      it(s"$complex is ${if (isReal) "" else "not "}a real number") {
        Real(complex) should matchPattern {
          case Right(_: Real) if isReal =>
          case Left(_) if !isReal       =>
        }
      }

    test(Complex(0, 0), isReal = true)
    test(Complex(-100, 0), isReal = true)
    test(Complex(100, 0), isReal = true)
    test(Complex(0.5, -0.5), isReal = false)
    test(Complex(0, 100), isReal = false)
    test(Complex(0, -100), isReal = false)
  }

  describe("Imaginary") {
    def test(complex: Complex, isImaginary: Boolean): Unit =
      it(s"$complex is ${if (isImaginary) "" else "not "}an imaginary number") {
        Imaginary(complex) should matchPattern {
          case Right(_: Imaginary) if isImaginary =>
          case Left(_) if !isImaginary            =>
        }
      }

    test(Complex(0, 0), isImaginary = false)
    test(Complex(-100, 0), isImaginary = false)
    test(Complex(100, 0), isImaginary = false)
    test(Complex(0.5, -0.5), isImaginary = false)
    test(Complex(0, 100), isImaginary = true)
    test(Complex(0, -100), isImaginary = true)

  }
}
