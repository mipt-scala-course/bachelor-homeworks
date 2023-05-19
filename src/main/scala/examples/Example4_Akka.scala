package examples

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.ClosedShape
import akka.stream.scaladsl.*

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.{ExecutionContextExecutor, Future}

object Example4_Akka:
  given system: ActorSystem = ActorSystem("Example4_Akka")

  val source1: Source[Int, NotUsed] = Source(1 to 100)
  val source2: Source[Int, NotUsed] = Source(1001 to 1100)

  val source: Source[Int, NotUsed] = Source.combine(source1, source2)(Merge(_))

  @main def e4: Unit = source.runForeach(println)

  given ExecutionContextExecutor = system.dispatcher
  def completeFuture(f: Future[Done]): Unit = f.onComplete(_ => system.terminate().onComplete(println))

  @main def e4_2: Unit = completeFuture(source.runForeach(println))

  val prod: Source[BigInt, NotUsed] = source.fold(BigInt(1))(_ * _)

  @main def e4_3: Unit = completeFuture(prod.runForeach(println))

  val source3 = Source(2001 to 2010).throttle(1, FiniteDuration(1, TimeUnit.SECONDS))

  @main def e4_4: Unit = completeFuture(source3.runForeach(println))

  val fullSource = Source.combine(source1, source2, source3)(Merge(_))

  @main def e4_5: Unit = completeFuture(fullSource.runForeach(println))

  val x2flow: Flow[Int, Int, NotUsed] = Flow.fromFunction(_ * 2)
  val printSink: Sink[Int, Future[Unit]] = Sink.fold(())((_, a) => println(a))

  val graph = GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    source ~> x2flow ~> printSink
    ClosedShape
  }

  @main def e4_6: Unit = RunnableGraph.fromGraph(graph).run()
