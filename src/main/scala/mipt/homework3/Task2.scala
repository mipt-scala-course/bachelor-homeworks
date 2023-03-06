package mipt.homework3

import mipt.utils.Homeworks.TaskSyntax

/**
  * Задания в этом файле необходимо решать используя иммутабельные коллекции,
  * т.е. scala.collection._ и scala.collection.immutable._
  *
  * Любые методы из стандартной библиотеки в этом файле использовать можно.
  */
object Task2 {
  def removeMostFrequent(numbers: Seq[Int]): Seq[Int] =
    task"""
           Реализуйте метод removeMostFrequent.
           В списке чисел нужно найти число с самым большим числом повторений, и вернуть новый список без этого числа
           Если есть несколько разных чисел с одинаковой (максимальной) частотой, то удалить их все
        """ (2, 1)

  def smoothNumbers(numbers: Seq[Int]): Seq[Double] =
    task"""
          Реализуйте метод smoothNumbers.
          Для каждого элемента списка, нужно заменить его на среднее арифметическое этого элемента и двух соседних
          Если какого-то из соседних элементов нет, то среднее необходимо считать не по 3, а по 2 или 1 значению.
        """ (2, 2)

  case class User(lastName: String, firstName: String, middleName: String, age: Int)

  def sortUsers(users: Seq[User]): Seq[User] =
    task"""
          Реализуйте метод sortUsers.
          Есть список людей (фамилия, имя, отчество, возраст)
          Нужно отсортировать его в следующем порядке: фамилия (лекс) -> возраст (по убыванию) -> имя (лекс) -> отчество (лекс)
        """ (2, 3)

}
