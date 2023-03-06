package mipt.homework3

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class Task1Spec extends AnyFlatSpec with Matchers {

  import Task1._

  "take" should "take only N first elements or the whole list if N is more than list length" in new TestData {

    take(nonEmptyList, 2) shouldBe MyCons(1, MyCons(-1, MyNil))
    take(nonEmptyList, 0) shouldBe MyNil
    take(nonEmptyList, -1) shouldBe MyNil
    take(nonEmptyList, 5) shouldBe nonEmptyList

  }

  "takeRight" should "take only N last elements or the whole list if N is more than list length" in new TestData {

    takeRight(nonEmptyList, 2) shouldBe MyCons(12, MyCons(-4, MyNil))
    takeRight(nonEmptyList, 0) shouldBe MyNil
    takeRight(nonEmptyList, -1) shouldBe MyNil
    takeRight(nonEmptyList, 5) shouldBe nonEmptyList

  }

  "takeWhile" should "take first elements matching the predicate passed" in new TestData {

    takeWhile[Int](nonEmptyList, _ > 0) shouldBe MyCons(1, MyNil)
    takeWhile[Int](nonEmptyList, a => Math.abs(a) < 2) shouldBe MyCons(1, MyCons(-1, MyNil))
    takeWhile[Int](nonEmptyList, _ != 0) shouldBe nonEmptyList
    takeWhile[Int](nonEmptyList, _ == 0) shouldBe MyNil
  }

  "dropWhile" should "drop first elements matching the predicate passed" in new TestData {

    dropWhile[Int](nonEmptyList, _ > 0) shouldBe nonEmptyList.tail
    dropWhile[Int](nonEmptyList, a => Math.abs(a) < 2) shouldBe MyCons(12, MyCons(-4, MyNil))
    dropWhile[Int](nonEmptyList, _ != 0) shouldBe MyNil
    dropWhile[Int](nonEmptyList, _ == 0) shouldBe nonEmptyList

  }

  "zip" should "zip two lists" in new TestData {

    zip(nonEmptyList, nonEmptyList.tail) shouldBe MyCons((1, -1), MyCons((-1, 12), MyCons((12, -4), MyNil)))
    zip(nonEmptyList, MyNil) shouldBe MyNil

  }

  private trait TestData {

    val nonEmptyList = MyCons(1, MyCons(-1, MyCons(12, MyCons(-4, MyNil))))

  }

}
