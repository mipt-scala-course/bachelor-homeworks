package examples

import cats.effect.{IO, Ref}
import cats.effect.std.Queue
import cats.effect.unsafe.implicits.global
import fs2.{Pipe, Stream}

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration

object Example7_fs2Shop:
  type F[x] = IO[x]

  case class User(id: Int, name: String, address: String)
  enum Item:
    case Glasses
    case Monocle
    case MagnifyingGlass
    case Packaging(item: Item, count: Int)
  case class Order(items: Vector[Item], user: User)

  class Fs2Client(user: User, queue: Queue[F, Order]):
    val stream: Stream[F, Order] = Stream.repeatEval(queue.take)
    val receivePipe: Pipe[F, Order, Unit] =
      _.filter(_.user == user).foreach(o => IO.println(s"Successfully received order $o"))

    def makeOrder(items: Vector[Item]): F[Unit] = queue.offer(Order(items, user))

  object Fs2Client:
    def apply(user: User): F[Fs2Client] =
      Queue.bounded[F, Order](128).map(q => new Fs2Client(user, q))

  class Fs2Shop:
    val orderPipe: Pipe[F, Order, Order] =
      _.evalTap(o => IO.println(s"Shop received order $o, send request to factory"))
    val deliveryPipe: Pipe[F, Order, Order] =
      _.evalTap(o => IO.println(s"Received items of order $o, start delivery"))

  class Fs2Factory:
    val orderPipe: Pipe[F, Order, Order] = _.evalMap(o =>
      for {
        start  <- IO.realTimeInstant
        _      <- IO.println(s"Factory received order $o at $start, start producing")
        _      <- IO.sleep(FiniteDuration(100, TimeUnit.MILLISECONDS))
        finish <- IO.realTimeInstant
        _      <- IO.println(s"Order $o was produced at $finish, send back to shop")
      } yield o
    )

  @main def e7: Unit =
    val shop = new Fs2Shop
    val factory = new Fs2Factory

    (for {
      client1 <- Fs2Client(User(1, "John Doe", "somewhere"))
      client2 <- Fs2Client(User(2, "Steve Doe", "somewhere else"))
      orders          = client1.stream.merge(client2.stream)
      factoryRequests = orders.through(shop.orderPipe)
      produced        = factoryRequests.through(factory.orderPipe)
      delivered       = produced.through(shop.deliveryPipe)
      received1       = delivered.through(client1.receivePipe)
      received2       = delivered.through(client2.receivePipe)
      received        = received1.merge(received2)
      _ <- client1.makeOrder(Vector(Item.Glasses))
      _ <- client2.makeOrder(Vector(Item.Packaging(Item.MagnifyingGlass, 15)))
      _ <- client1.makeOrder(Vector(Item.Monocle, Item.Monocle))
      _ <- received.compile.drain
    } yield ()).unsafeRunSync()
