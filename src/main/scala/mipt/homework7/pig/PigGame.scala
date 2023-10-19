package mipt.homework7
package pig

import cats.Monad
import cats.effect.{IO, IOApp}
import cats.effect.std.Random

import scala.util.{Try, Success}
import scala.util.control.NoStackTrace

import Homeworks.*


object PigGame extends IOApp.Simple:

    // 6-гранный кубик
    enum Dice(val score: Int, val name: String):
        case D1 extends Dice(1, "⚀")
        case D2 extends Dice(2, "⚁")
        case D3 extends Dice(3, "⚂")
        case D4 extends Dice(4, "⚃")
        case D5 extends Dice(5, "⚄")
        case D6 extends Dice(6, "⚅")


    // Броски кубика
    trait Roll:
        def roll: IO[Dice]
        def rollN(n: Int, condition: Dice => Boolean): IO[List[Dice]]

    object Roll:
        case class ConditionFailed(rolls: List[Dice]) extends Exception() with NoStackTrace

        def apply(): Roll =
            new:
                override def roll: IO[Dice] =
                    task"""
                        1) Реализовать метод `roll: F[Dice]`, который описывает бросок кубика
                            На выходе: выпавшая рандомная сторона кубика

                            Для генерирования случайный чисел используйте:
                                import cats.effect.IO
                                import cats.effect.std.Random

                                Random.scalaUtilRandom[IO]
                        """ (7, 1)
                    

                override def rollN(n: Int, condition: Dice => Boolean): IO[List[Dice]] =
                    task"""
                        2) Реализовать метод `rollN(n: Int, condition: Dice => Boolean): IO[List[Dice]]`
                            `rollN` - описывает следующее действие:
                            подбрасываем кубик до тех пор пока выполняется `condition` на последнем броске, но не больше n раз

                            На выходе: список результатов бросков начиная с первого броска
                    """ (7, 2)
    

    // Игра, ход из некоторого числа бросков
    trait Game:
        def move: IO[Int]

    object Game:
        def apply(): Game =
            new:
                override def move: IO[Int] =
                    task"""
                        3) Реализовать метод `move: IO[Int]`
                            `move` - описывает ход в игре "Свинья" и возвращает число очков полученных за ход:
                                - Шаг1. Ввод
                                    Пользователь вводит число раз которое хочет кинуть кубик
                                - Шаг2. Бросок
                                    Если пользователь вводит число большее 0, то кидается кубик это число раз
                                    При любом другом вводе ход пользователя заканчивается и отображается количество очков которые он заработал за ход
                                - Шаг3. Подсчет очков
                                    Если среди выпавшего есть 1ка, то ход заканчивается, пользователь получает 0 очков за ход, независимо от предыдущих результатов
                                    Если нет 1ки, то суммируются очки (сколько выпало на кубиках + сколько у пользователя было очков ранее) и переходим к Шагу1

                            Примеры работы:
                                1)
                                    Счет за ход: 0. Сколько раз кидаете кубик?
                                    > 3
                                    ⚂ ⚄ ⚃
                                    Счет за ход: 12. Сколько раз кидаете кубик?
                                    > 1
                                    ⚃
                                    Счет за ход: 16. Сколько раз кидаете кубик?
                                    > 1
                                    ⚅
                                    Счет за ход: 22. Сколько раз кидаете кубик?
                                    > 0
                                    Ход окончен, за этот ход вы набрали: 22
                                2)
                                    Счет за ход: 0. Сколько раз кидаете кубик?
                                    > 3
                                    ⚅ ⚀
                                    Эх, не повезло
                                    Ход окончен, за этот ход вы набрали: 0
                    """ (7, 3)


    override def run: IO[Unit] =
        Game().move.as(())

