package mipt.homework8

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import mipt.homework8.Task1._

import java.util.concurrent.atomic.AtomicInteger
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

class Task1Spec extends AnyFlatSpec with Matchers {

  "foldF" should "fold list of futures into a single future using combining function" in new WithGlobalExecutionContext {

    val input = Seq(1, 2, 3, 4, 5, 6).map(Future.successful)

    await(foldF[Int, Int](input, 0, _ + _)) shouldBe 21
    await(foldF[Int, Int](input, 1, _ * _)) shouldBe 720

  }

  "flatFoldF" should "fold list of futures into a single future using async combining function" in new WithGlobalExecutionContext {

    val input   = Seq(1, 2, 3, 4, 5, 6).map(Future.successful)
    val sum     = (a: Int, b: Int) => Future.successful(a + b)
    val product = (a: Int, b: Int) => Future.successful(a * b)

    await(flatFoldF(input, 0, sum)) shouldBe 21
    await(flatFoldF(input, 1, product)) shouldBe 720

  }

  "full sequence" should "process list of success futures" in new WithLimitedExecutionContext {

    /**
      * best answer will process task with 9 runnable
      * good answer will process task with 12 runnable
      * satisfied answer will process task with any number of runnable
      * choose which one you want
      * */
    val limit = 100

    implicit val exec: ExecutionContext = limitedExec(limit)
    val fut1                            = fut(1)
    val fut2                            = fut(2)
    val fut3                            = fut(3)

    assert(await(fullSequence[Int](List(fut1, fut2, fut3))) === (List(1, 2, 3), List()))
  }

  it should "process list of success and failures" in new WithLimitedExecutionContext {

    /**
      * best answer will process task with 7 runnable
      * good answer will process task with 8 runnable
      * satisfied answer will process task with any number of runnable
      * choose which one you want
      * */
    val limit = 100

    implicit val exec: ExecutionContext = limitedExec(limit)
    val ex1                             = new Exception("ex1")
    val ex2                             = new Exception("ex2")
    val failed1                         = Future.failed(ex1)
    val failed2                         = Future.failed(ex2)
    val fut1                            = fut(1)

    assert(await(fullSequence[Int](List(fut1, failed1, failed2))) === (List(1), List(ex1, ex2)))
  }

  it should "process list of failures" in new WithLimitedExecutionContext {

    /**
      * best answer will process task with 4 runnable
      * satisfied answer will process task with any number of runnable
      * choose which one you want
      * */
    val limit = 100

    implicit val exec: ExecutionContext = limitedExec(limit)
    val ex1                             = new Exception("ex1")
    val ex2                             = new Exception("ex2")
    val failed1                         = Future.failed(ex1)
    val failed2                         = Future.failed(ex2)

    assert(await(fullSequence[Int](List(failed1, failed2))) === (List(), List(ex1, ex2)))
  }

  "traverse via sequence" should "behave as a scala Future.traverse" in new WithGlobalExecutionContext {

    val xs = (1 to 10).toList

    val result = await(traverse(xs)(fut))

    assert(result === await(Future.traverse(xs)(fut)))
    assert(result === xs)
  }

  it should "work with empty lists" in new WithGlobalExecutionContext {
    assert(await(traverse(Nil)(fut)) === Nil)
  }

  it should "correctly stop on failures" in new WithGlobalExecutionContext {
    case class MyError() extends Exception

    val xs = (1 to 100).toList

    try {
      await(traverse(xs)(v => {
        if (v == 42) throw MyError();
        fut(v)
      }))
      assert(false)
    } catch {
      case MyError() => assert(true)
    }
  }

  "mapReduce" should "asynchronously map and reduce the list and skip failures" in new WithGlobalExecutionContext {

    val xs                        = (1 to 10).toList
    val predicate: Int => Boolean = a => a == 5 || a == 6
    val map: Int => Future[Int] = a =>
      if (predicate(a))
        Future.failed(new RuntimeException)
      else Future.successful(a * 2)

    val reduce: (Int, Int) => Int = _ + _
    await(mapReduce(xs, map, reduce)) shouldBe xs.filterNot(predicate).map(_ * 2).sum

  }

  it should "throw UnsupportedOperationException if all elements mapping is failed" in new WithGlobalExecutionContext {

    val xs                        = (1 to 10).toList
    val map: Int => Future[Int]   = _ => Future.failed(new RuntimeException)
    val reduce: (Int, Int) => Int = _ + _

    try {
      await(mapReduce(xs, map, reduce))
      assert(false)
    } catch {
      case _: UnsupportedOperationException => assert(true)
      case _: Throwable                     => assert(false)
    }

  }

  def await[A](future: Future[A]): A = Await.result(future, Duration.Inf)

  def fut(i: Int)(implicit ex: ExecutionContext): Future[Int] = Future {
    Thread.sleep(1000)
    i
  }

  trait WithLimitedExecutionContext {
    def limitedExec(limit: Int): ExecutionContext = new ExecutionContext {
      val counter                  = new AtomicInteger(0)
      val global: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global

      override def execute(runnable: Runnable): Unit = {
        counter.incrementAndGet()
        if (counter.get() > limit) {
          throw new Exception("Runnable limit reached, You can do better :)")
        } else {
          global.execute(runnable)
        }
      }

      override def reportFailure(cause: Throwable): Unit = ???
    }

  }

  trait WithGlobalExecutionContext {

    implicit val executionContext: ExecutionContext = scala.concurrent.ExecutionContext.global

  }

}
