package mipt.homework9

import mipt.utils.Homeworks._

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

object Task2 {

  implicit class TFutureRecover[T](val tFuture: TFuture[T]) extends AnyVal {

    def recoverWith[U >: T](pf: PartialFunction[Throwable, TFuture[U]])(implicit ec: ExecutionContext): TFuture[U] =
      task"""
            Реализовать метод recoverWith, который аналогичен по поведению методу recoverWith стандартной Future.
            Если исходная tFuture завершилась успехом - возвращать ее результат
            Если исходная tFuture завершилась исключением, которое не обрабатывается частичной функцией - возвращать ее результат
            Если исходная tFuture завершилась исключением, которое обрабатывается частичной функцией - обработать его асинхронно
            Если попытка обработать исключение провалилась - вернуть зафейленную фьючу с этим исключением
            При реализации пользоваться TFuture и TPromise
          """ (2, 1)

  }

}
