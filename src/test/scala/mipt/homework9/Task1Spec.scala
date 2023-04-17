package mipt.homework9

import org.scalatest.flatspec.AnyFlatSpec

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global


import Task1.parTraverse

class Task1Spec extends AnyFlatSpec {
  def await[A](future: Future[A]): A = Await.result(future, Duration.Inf)

  def fut(i: Int): Future[Int] = Future {
    Thread.sleep(1)
    i
  }

  case class MyError() extends Exception

  def throwingFut(i: Int): Future[Int] = Future {
    Thread.sleep(1)
    if (i == 42) throw MyError()
    i
  }

  "parTraverse" should "correctly work with big lists" in {
    val limit = 10

    val hugeList = List.fill(10000)(42)

    assert(await(parTraverse(limit)(hugeList)(fut)) === hugeList)
  }

  it should "correctly work with empty list" in {
    val limit = 1

    val emptyList = Nil

    assert(await(parTraverse(limit)(emptyList)(fut)) === emptyList)
  }

  it should "correctly stop on failures" in {
    val parallelismLimit = 10

    val hugeList = (1 to 1000).toList

    try {
      await(parTraverse(parallelismLimit)(hugeList)(throwingFut))
      assert(false)
    } catch {
      case MyError() => assert(true)
    }
  }

}
