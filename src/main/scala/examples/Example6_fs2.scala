package examples

import cats.effect.IO
import cats.effect.std.Queue
import cats.effect.unsafe.implicits.global
import fs2.{Pipe, Stream}

import java.util.concurrent.TimeUnit
import scala.concurrent.duration.FiniteDuration

object Example6_fs2:
  @main def e6: Unit =
    println(Stream.empty.toList)
    println(Stream(1, 2, 3, 4, 5).toList)
    println(Stream.emits(1 to 100).toList)

  @main def e6_2: Unit =
    Stream.eval(IO {
      println("I'm working")
      2 + 2
    }).compile.toVector.map(println).unsafeRunSync()

  @main def e6_3: Unit =
    Stream.eval(IO {
      println("I'm working")
      2 + 2
    }).repeatN(5).compile.toVector.map(println).unsafeRunSync()

  @main def e6_4: Unit =
    Stream.eval(IO {
      println("I'm working")
      2 + 2
    }).compile.drain.unsafeRunSync()

  @main def e6_5: Unit =
    Stream.evalSeq(IO.pure(1 to 100)).chunkN(10).compile.toVector.map(println).unsafeRunSync()

  @main def e6_6: Unit =
    val pipe: Pipe[IO, Int, Unit] = _.foreach(i => IO { println(i) })
    Stream.evalSeq(IO.pure(1 to 100)).through(pipe).compile.toVector.map(println).unsafeRunSync()

  @main def e6_7: Unit =
    (for {
      q1 <- Stream.eval(Queue.bounded[IO, Int](1))
      q2 <- Stream.eval(Queue.bounded[IO, Int](100))
      start = Stream(
        Stream.range(0, 1000).covary[IO].foreach(q1.offer),
        Stream.repeatEval(q1.take).foreach(q2.offer),
        Stream.repeatEval(q2.take).foreach(n => IO { println(s"Pulling out $n from Queue #2") })
      ).parJoin(3)
      _ <- Stream.sleep[IO](FiniteDuration(5, TimeUnit.SECONDS)) concurrently start.drain
    } yield ()).compile.drain.unsafeRunSync()
