package mipt.homework4

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import Task1._

class Task1Spec extends AnyFlatSpec with Matchers {

  "filter" should "filter the stream and return a new stream" in {
    println(fibonacci.take(10))

    fibonacci.filter(_ % 2 == 0).take(4) shouldBe List(0, 2, 8, 34)
  }

  "dropWhile" should "drop first appropriate elements and return a new stream" in {
    fibonacci.dropWhile(_ < 10).take(3) shouldBe List(13, 21, 34)
  }

  "flatMap" should "work lazy and return a new stream" in {
    fibonacci.flatMap { a => MyStream(a, MyStream(a, MyStream(a, Empty))) }.take(15) shouldBe List(0, 0, 0, 1, 1, 1, 1,
      1, 1, 2, 2, 2, 3, 3, 3)
  }

  // Бесконечная последовательность чисел Фибоначчи
  // 0, 1, 1, 2, 3, 5, 8, 13, 21, 34...
  val fibonacci: MyStream[Int] = {
    def nextValue(prePrev: Int, prev: Int): MyStream[Int] = {
      val current = prePrev + prev
      MyStream(current, nextValue(prev, current))
    }

    MyStream(0, MyStream(1, nextValue(0, 1)))
  }

}
