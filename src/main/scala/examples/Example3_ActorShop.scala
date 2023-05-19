package examples

import java.time.LocalDateTime
import scala.collection.mutable

object Example3_ActorShop:
  case class User(id: Int, name: String, address: String)
  enum Item:
    case Glasses
    case Monocle
    case MagnifyingGlass
    case Packaging(item: Item, count: Int)
  case class Order(items: Vector[Item], user: User)

  trait Client:
    def makeOrder(items: Vector[Item]): Unit

  class ActorClient(user: User) extends Client:
    val userId: Int = user.id
    var shop: Option[ActorShop] = None

    override def makeOrder(items: Vector[Item]): Unit =
      shop.get.receiveOrder(Order(items, user))

    def receiveOrder(order: Order): Unit =
      println(s"Received order $order")

  trait Shop:
    def addNewClient(client: Client): Unit

  class ActorShop extends Shop:
    val clients: mutable.Set[ActorClient] = mutable.Set.empty
    var factory: Option[ActorFactory] = None

    override def addNewClient(client: Client): Unit = {
      clients.add(client.asInstanceOf[ActorClient])
      client.asInstanceOf[ActorClient].shop = Some(this)
    }

    def receiveOrder(order: Order): Unit = {
      println(s"Shop received order $order")
      factory.get.produceOrder(order)
      clients.find(_.userId == order.user.id).get.receiveOrder(order)
    }

  trait Factory:
    def subscribeShop(shop: Shop): Unit

  class ActorFactory extends Factory:
    override def subscribeShop(shop: Shop): Unit =
      shop.asInstanceOf[ActorShop].factory = Some(this)

    def produceOrder(order: Order): Unit = {
      println(s"Factory received order $order at ${LocalDateTime.now()}")
      Thread.sleep(100)
      println(s"Order $order was produced at ${LocalDateTime.now()}")
    }

  @main def e3: Unit =
    val client1 = new ActorClient(User(1, "John Doe", "somewhere"))
    val client2 = new ActorClient(User(2, "Steve Doe", "somewhere else"))
    val shop    = new ActorShop
    val factory = new ActorFactory

    shop.addNewClient(client1)
    shop.addNewClient(client2)
    factory.subscribeShop(shop)

    client1.makeOrder(Vector(Item.Glasses))
    client2.makeOrder(Vector(Item.Packaging(Item.MagnifyingGlass, 15)))
    client1.makeOrder(Vector(Item.Monocle, Item.Monocle))
