package mipt.homework9

import scala.concurrent.{ExecutionContext, Future}
import mipt.utils.Homeworks._

object Task1 {

  def parTraverse[A, B](parallelism: Int)(xs: List[A])(fa: A => Future[B])(
      implicit ex: ExecutionContext
  ): Future[List[B]] =
    task"""
          Реализуйте аналог Future.traverse,
          у которого будет ограничено число одновременно выполняющихся Future
          Задачи должны выполняться на контекте исполнения, который есть в скоупе
        """ (1, 1)

}
