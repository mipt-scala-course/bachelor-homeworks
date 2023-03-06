package mipt.homework3

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

import mipt.homework3.Task2._

class Task2Spec extends AnyFunSuite with Matchers {
  test("Удаление самого частого числа") {
    removeMostFrequent(Seq()) shouldBe Seq()
    removeMostFrequent(Seq(1)) shouldBe Seq()
    removeMostFrequent(Seq(1, 2, 3, 2, 4, 3, 2)) shouldBe Seq(1, 3, 4, 3)
    removeMostFrequent(Seq(1, 2, 3, 2, 4, 3, 2, 3)) shouldBe Seq(1, 4)
  }

  test("Сглаживание списка чисел") {
    smoothNumbers(Seq()) shouldBe Seq()
    smoothNumbers(Seq(3)) shouldBe Seq(3.0d)
    smoothNumbers(Seq(1, 4)) shouldBe Seq(2.5d, 2.5d)
    smoothNumbers(Seq(1, 9, 5, 4, 3, 5, 5, 5, 8, 2)) shouldBe Seq(5.0, 5.0, 6.0, 4.0, 4.0, 13.0 / 3, 5.0, 6.0, 5.0, 5.0)
  }

  test("Сортировка по фамилии и возрасту") {
    val sorted = sortUsers(
      Seq(
        User("Сидоров", "Виктор", "Львович", 30),
        User("Иванов", "Петр", "Петрович", 42),
        User("Петров", "Василий", "Абрамович", 8),
        User("Сидоров", "Александр", "Григорьевич", 30),
        User("Иванов", "Иван", "Иванович", 34),
        User("Сидоров", "Артем", "Викторович", 33)
      )
    )

    sorted shouldBe Seq(
      User("Иванов", "Петр", "Петрович", 42),
      User("Иванов", "Иван", "Иванович", 34),
      User("Петров", "Василий", "Абрамович", 8),
      User("Сидоров", "Артем", "Викторович", 33),
      User("Сидоров", "Александр", "Григорьевич", 30),
      User("Сидоров", "Виктор", "Львович", 30)
    )
  }
}
