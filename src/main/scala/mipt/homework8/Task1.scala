package mipt.homework8

import scala.concurrent.{ExecutionContext, Future}
import mipt.utils.Homeworks._

object Task1 {

  def foldF[A, B](in: Seq[Future[A]], zero: B, op: (B, A) => B)(
      implicit executionContext: ExecutionContext
  ): Future[B] =
    task"""
          Реализуйте функцию, которая выполнит свертку (fold) входящей последовательности из Future,
          используя переданный комбинатор и начальное значение для свертки.
          Если какая-либо из исходных Future зафейлилась, то должна вернуться ошибка от нее
        """ (1, 1)

  def flatFoldF[A, B](in: Seq[Future[A]], zero: B, op: (B, A) => Future[B])(
      implicit executionContext: ExecutionContext
  ): Future[B] =
    task"""
          Реализуйте функцию, которая выполнит свертку (fold) входящей последовательности из Future,
          используя переданный асинхронный комбинатор и начальное значение для свертки.
          Если какая-либо из исходных Future зафейлилась, то должна вернуться ошибка от нее.
          Если комбинатор зафейлился, то должна вернуться ошибка от него.
        """ (1, 2)

  def fullSequence[A](futures: List[Future[A]])(
      implicit ex: ExecutionContext
  ): Future[(List[A], List[Throwable])] =
    task"""
          В данном задании Вам предлагается реализовать функцию fullSequence,
          похожую на Future.sequence, но в отличии от нее,
          возвращающую все успешные и не успешные результаты.
          Возвращаемое тип функции - кортеж из двух списков,
          в левом хранятся результаты успешных выполнений,
          в правово результаты неуспешных выполнений.
          Не допускается использование методов объекта Await и мутабельных переменных var
        """ (1, 3)

  def traverse[A, B](in: List[A])(fn: A => Future[B])(
      implicit ex: ExecutionContext
  ): Future[List[B]] =
    task"""
          Реализуйте traverse c помощью метода Future.sequence
        """ (1, 4)

  def mapReduce[A, B, B1 >: B](in: List[A], map: A => Future[B], reduce: (B1, B1) => B1)(
      implicit ex: ExecutionContext
  ): Future[B1] =
    task"""
          Реализуйте алгоритм map/reduce.
          Исходный список обрабатывается параллельно (конкурентно) с помощью применения функции map к каждому элементу
          Результаты работы функции map должны быть свернуты в одно значение функцией reduce
          Если в ходе выполнения какой-либо операции возникло исключение - эту обработку нужно игнорировать
          Если ни один вызов map не завершился успешно, вернуть зафейленную фьючу с исключением UnsupportedOperationException
        """ (1, 5)

}
