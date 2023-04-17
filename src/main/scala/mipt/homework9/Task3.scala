package mipt.homework9

import mipt.utils.Homeworks._

object Task3 {

  trait EventLoopFunction[A, B] {

    def apply(a: A, cb: B => Unit): Unit

  }

  def eventLoop[A, B](f: A => B): EventLoopFunction[A, B] =
    task"""
         Реализовать event loop обертку вокруг фукции
         Эта обертка из синхронной функции делает асинхронную, добавляя ей параметр callback
         Все вызовы такой асинхронной функции должны выполняться строго по порядку вызова в коде

         Пример использования:
         val f: Int => Int = _ * 2
         val asyncF = eventLoop(f)

         asyncF(10, a => println(a)) // асинхронно напечатает 20
         asyncF(5, a => println(a)) // асинхронно напечатает 10 после 20
      """ (3, 1)

}
