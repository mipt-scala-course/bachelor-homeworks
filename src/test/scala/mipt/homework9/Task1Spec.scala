package mipt.homework9

import org.scalatest.flatspec.AnyFlatSpec

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import Task1.parTraverse

import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

class Task1Spec extends AnyFlatSpec {
  def await[A](future: Future[A]): A = Await.result(future, Duration.Inf)

  def fut(i: Int)(implicit ec: ExecutionContext): Future[Int] = Future {
    Thread.sleep(1)
    i
  }

  case class MyError() extends Exception

  def throwingFut(i: Int)(implicit ec: ExecutionContext): Future[Int] = Future {
    Thread.sleep(1)
    if (i == 42) throw MyError()
    i
  }

  "parTraverse" should "correctly work with big lists" in {
    import scala.concurrent.ExecutionContext.Implicits.global

    val limit = 10

    val hugeList = List.fill(10000)(42)

    assert(await(parTraverse(limit)(hugeList)(fut)) === hugeList)
  }

  it should "correctly work with empty list" in {
    import scala.concurrent.ExecutionContext.Implicits.global

    val limit = 1

    val emptyList = Nil

    assert(await(parTraverse(limit)(emptyList)(fut)) === emptyList)
  }

  it should "correctly stop on failures" in {
    import scala.concurrent.ExecutionContext.Implicits.global

    val parallelismLimit = 10

    val hugeList = (1 to 1000).toList

    try {
      await(parTraverse(parallelismLimit)(hugeList)(throwingFut))
      assert(false)
    } catch {
      case MyError() => assert(true)
    }
  }

  it should "control parallelism" in {

    implicit val executionContext: ExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(10))

    val counter: AtomicInteger = new AtomicInteger()
    val func: Int => Future[Int] = i =>
      Future {
        Thread.sleep(100)
        counter.incrementAndGet()
        i
      }

    val list = (1 to 10).toList
    parTraverse(2)(list)(func)

    Thread.sleep(110)
    assert(counter.get() == 2)

  }

}
