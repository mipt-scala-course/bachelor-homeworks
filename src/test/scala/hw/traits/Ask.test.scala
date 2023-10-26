package hw.traits

import cats.data.{Reader, ReaderT}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class AskTests extends AnyFlatSpec with Matchers:
  it should "create Ask[Reader] instance" in {
    Ask[Reader[Int, *], Int].ask.run(42) shouldBe 42
  }

  it should "create Ask[ReaderT] instance" in {
    Ask[ReaderT[Option, Int, *], Int].ask.run(42) shouldBe Some(42)
  }
