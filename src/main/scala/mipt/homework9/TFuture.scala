package mipt.homework9

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success, Try}

trait TPromise[T] {

  def future: TFuture[T]

  def completeWith(f: TFuture[T]): Unit

  def fail(th: Throwable): Unit

}

object TPromise {

  def apply[T]: TPromise[T] = new TPromise[T] {

    val syncObj                                = new Object
    val nonEmptyResultGuard: Try[T] => Nothing = _ => throw new IllegalArgumentException

    @volatile var _result: Option[Try[T]] = None
    var _callbacks: List[Try[T] => Unit]  = List.empty

    def mapOrElseResult(f: Try[T] => Unit)(orElse: => Unit): Unit =
      _result.map(f).getOrElse {
        syncObj.synchronized {
          _result.map(f).getOrElse(orElse)
        }
      }

    override val future: TFuture[T] = new TFuture[T] {

      override def flatMap[U](f: T => TFuture[U])(implicit ec: ExecutionContext): TFuture[U] = {
        val promise = TPromise[U]
        val callback: Try[T] => Unit = {
          case Success(value) =>
            ec.execute { () =>
              Try(f(value)) match {
                case Success(res) => promise.completeWith(res)
                case Failure(ex)  => promise.fail(ex)
              }
            }
          case Failure(ex) =>
            promise.fail(ex)
        }
        onComplete(callback)
        promise.future
      }

      override def onComplete(f: Try[T] => Unit): Unit =
        mapOrElseResult(f) {
          _callbacks = f :: _callbacks
        }

    }

    override def completeWith(f: TFuture[T]): Unit =
      f.onComplete { res =>
        mapOrElseResult(nonEmptyResultGuard) {
          _result = Some(res)
          _callbacks.foreach(_(res))
        }
      }

    override def fail(th: Throwable): Unit =
      mapOrElseResult(nonEmptyResultGuard) {
        _result = Some(Failure(th))
        _callbacks.foreach(_(Failure(th)))
      }

  }

}

trait TFuture[+T] {

  def flatMap[U](f: T => TFuture[U])(implicit ec: ExecutionContext): TFuture[U]

  def map[U](f: T => U)(implicit ec: ExecutionContext): TFuture[U] =
    flatMap(f andThen TFuture.pure)

  def onComplete(f: Try[T] => Unit): Unit

}

object TFuture {

  def pure[T](value: T): TFuture[T] = new TFuture[T] {

    override def flatMap[U](f: T => TFuture[U])(implicit ec: ExecutionContext): TFuture[U] = {
      val promise = TPromise[U]
      ec.execute { () =>
        Try(f(value)) match {
          case Success(succeeded) =>
            promise.completeWith(succeeded)
          case Failure(exception) =>
            promise.fail(exception)
        }
      }
      promise.future
    }

    override def onComplete(f: Try[T] => Unit): Unit = f(Success(value))

  }

  lazy val unit: TFuture[Unit] = pure(())

  def apply[T](body: => T)(implicit ec: ExecutionContext): TFuture[T] =
    unit.map(_ => body)

}
