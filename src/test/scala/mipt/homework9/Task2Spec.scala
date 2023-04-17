package mipt.homework9

import org.scalatest.flatspec.AnyFlatSpec
import Task2._
import org.scalatest.matchers.should.Matchers

import scala.util.{Failure, Success, Try}

class Task2Spec extends AnyFlatSpec with Matchers {

  import scala.concurrent.ExecutionContext.Implicits.global

  def await[T](t: TFuture[T]): Try[T] = {

    var result: Option[Try[T]] = None
    t.onComplete { res =>
      result = Some(res)
    }

    while (result.isEmpty) Thread.sleep(10)
    result.get
  }

  "recoverWith" should "not run anything if TFuture is successful" in {

    val f = TFuture(10).recoverWith {
      case _: Throwable => TFuture.pure(20)
    }

    await(f) shouldBe Success(10)

  }

  it should "return failed TFuture with the entire throwable if it's not processable" in {

    val ex = new RuntimeException
    val f: TFuture[Int] = TFuture(throw ex).recoverWith {
      case _: ArithmeticException => TFuture.pure(10)
    }

    await(f) shouldBe Failure(ex)

  }

  it should "return throwable recover result if it is processable" in {

    val f = TFuture(10 / 0).recoverWith {
      case _: ArithmeticException => TFuture.pure(0)
    }
    await(f) shouldBe Success(0)
  }

  it should "return failed TFuture with throwable occurred in recovery function" in {

    val ex = new RuntimeException
    val f = TFuture(10 / 0).recoverWith {
      case _: ArithmeticException => throw ex
    }

    await(f) shouldBe Failure(ex)
  }

}
