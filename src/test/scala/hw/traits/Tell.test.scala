package hw.traits

import cats.data.{Writer, WriterT}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class TellTests extends AnyFlatSpec with Matchers:
  it should "create Tell[Writer] instance" in {
    Tell[Writer[Int, *], Int].tell(42).run shouldBe (42, ())
  }

  it should "create Tell[WriterT] instance" in {
    Tell[WriterT[Option, Int, *], Int].tell(42).run shouldBe Some((42, ()))
  }
