package context

import cats.FlatMap
import cats.mtl.Ask
import glass.Contains

/**
 * Реализуйте набор вспомогательных функций для работы с контекстом
 */
trait ContextOps[Context, F[_]]:
  /**
   * Реализуйте метод, возвращающий контекст
   */
  def getContext(using Ask[F, Context]): F[Context] = ???

  /**
   * Реализуйте метод, возвращающий значение, содержащееся в контексте
   */
  def getSubContext[A](using Ask[F, Context], Contains[Context, A]): F[A] = ???

  /**
   * Реализуйте метод, возвращающий Embed алгебру, содержащуюся в контексте
   */
  def getEmbedSubContext[U[_[_]]](using Ask[F, Context], Contains[Context, U[F]], Embed[U], FlatMap[F]): U[F] = ???
