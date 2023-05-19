package examples

import java.time.{Clock, LocalDateTime}
import scala.collection.mutable
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.util.Random

object Example1_ReadingSomeData:
  case class Data(s: String)

  def getData: Data = Data(Random.nextString(100))

  val queue: mutable.Queue[Data] = mutable.Queue.empty

  def producer: Unit =
    while (true) {
      Thread.sleep(200)
      queue.enqueue(getData)
    }

  def consumer: Unit =
    while (true) {
      println(queue.size)
      if (queue.nonEmpty) {
        val data = queue.dequeue()
        Thread.sleep(95)
        println(LocalDateTime.now())
        println(data)
      }
    }

  @main def e1: Unit = {
    given ExecutionContext = ExecutionContext.global

    val p = Future(producer)
    val c = Future(consumer)

    Await.result(p.flatMap(_ => c), Duration.Inf)
  }

  @main def e1_2: Unit = {
    given ExecutionContext = ExecutionContext.global

    val p1 = Future(producer)
    val p2 = Future(producer)
    val c = Future(consumer)

    Await.result(for {
      _ <- p1
      _ <- p2
      _ <- c
    } yield (), Duration.Inf)
  }
