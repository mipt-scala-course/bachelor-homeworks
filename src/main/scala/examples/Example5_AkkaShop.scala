package examples

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.{Attributes, ClosedShape, FanInShape2, Inlet, Outlet}
import akka.stream.stage.{GraphStage, GraphStageLogic}
import akka.stream.scaladsl.*

import java.time.{Clock, LocalDateTime}
import scala.concurrent.Future

object Example5_AkkaShop:
  case class User(id: Int, name: String, address: String)
  enum Item:
    case Glasses
    case Monocle
    case MagnifyingGlass
    case Packaging(item: Item, count: Int)
  case class Order(items: Vector[Item], user: User)

  given ActorSystem = ActorSystem("AkkaShop")

  class AkkaClient(user: User):
    val (orders, source) = Source.queue[Order](128).preMaterialize()
    val sink: Sink[Order, Future[Unit]] = 
      Sink.fold(())((_, o) => if (o.user == user) println(s"Received order $o"))

    def makeOrder(items: Vector[Item]): Unit = orders.offer(Order(items, user))

  class AkkaShop:
    val orderFlow: Flow[Order, Order, NotUsed] = 
      Flow[Order].map { o => // Note: there's not a wireTap because it runs it parallel
        println(s"Shop received order $o")
        o
      }
    val deliveryFlow: Flow[Order, Order, NotUsed] = Flow[Order].map { o =>
      println(s"Deliver order $o")
      o
    }

  class AkkaFactory:
    val orderFlow: Flow[Order, Order, NotUsed] = Flow[Order].map { o =>
      println(s"Factory received order $o at ${LocalDateTime.now()}")
      Thread.sleep(100)
      println(s"Order $o was produced at ${LocalDateTime.now()}")
      o
    }

  @main def e5: Unit =
    val shop = new AkkaShop
    val factory = new AkkaFactory

    val client1 = new AkkaClient(User(1, "John Doe", "somewhere"))
    val client2 = new AkkaClient(User(2, "Steve Doe", "somewhere else"))

    val graph = GraphDSL.create() { implicit b =>
      import GraphDSL.Implicits._

      val clientsMerge = b.add(Merge[Order](2))
      client1.source ~> clientsMerge.in(0)
      client2.source ~> clientsMerge.in(1)

      val clientsBroadcast = b.add(Broadcast[Order](2))
      clientsMerge.out ~> shop.orderFlow ~> factory.orderFlow ~> shop.deliveryFlow ~> clientsBroadcast.in
      clientsBroadcast.out(0) ~> client1.sink
      clientsBroadcast.out(1) ~> client2.sink

      ClosedShape
    }

    RunnableGraph.fromGraph(graph).run()

    client1.makeOrder(Vector(Item.Glasses))
    client2.makeOrder(Vector(Item.Packaging(Item.MagnifyingGlass, 15)))
    client1.makeOrder(Vector(Item.Monocle, Item.Monocle))
