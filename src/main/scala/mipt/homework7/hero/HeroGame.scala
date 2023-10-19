package mipt.homework7
package hero

import zio.*

import java.io.IOException

import Homeworks.*


object HeroGame extends ZIOAppDefault:

    // Снаряжение героя
    trait Weapon:
        def speed: Int
        def maxDamage: Int

    object Weapon:
        case class Impl(speed: Int, maxDamage: Int) extends Weapon
        lazy val tinSword: Weapon = Impl(2, 3)

    // Исключительные состояния враждебного моба
    enum MobError:
        case MobIsDead
        case MobAttacs

    // Враждебный моб
    trait Mob:
        def hp: Int // Показатель здоровья моба
        def to: Int // время на подготовку к удару
        def prepare(damage: Int): ZIO[Any, MobError, Mob]

    object Mob:
        case class Impl(hp: Int, to: Int) extends Mob:
            override def prepare(damage: Int): ZIO[Any, MobError, Mob] =
                task"""
                    Реализуйте проверку хода с точки зрения огра:
                        Если нанесённый урон больше оставшегося здоровья, то ход заканчивается с MobIsDead
                        Если это последний ход (to == 1), то ход заканчивается с MobAttacs
                        В ином случае возвращаем новое состояние огра (уменьшаем зоровье и время на подготовку)
                """ (7, 4)


        lazy val slimOgre = Impl(10, 3)

    // Герой
    trait Hero:
        def attack(foe: Mob): ZIO[Any, MobError, Mob]

    object Hero:
        def make(sword: Weapon): Hero =
            task"""
                Опишите героя такого, который мог бы атаковать выбранным оружием указанного противника
                    Подсчитайте урон с помощью zio.Random для заданных параметров оружия (урон и скорость)
                    Выведите в консоль строку вида "Hero deals <здесь подсчитаный урон> damage"
                    Нанесите выситанный урон противнику и получите новое состояние
                """ (7, 5)


    trait Fighting:
        def fight: ZIO[Any, IOException, Unit]

    object Fighting:
        def make(hero: Hero, foe: Mob): Fighting =
            task"""
                Опишите алгоритм сражения такой, который будет атаковать полученным героем указанного противника 
                пока не наступит победа одного из них
            
                Например:
                    Hero deals 2 damage
                    Hero deals 4 damage
                    Hero deals 6 damage
                    You win!
                        
                Или
                    Hero deals 2 damage
                    Hero deals 2 damage
                    Hero deals 4 damage
                    You die!
                """ (7, 6)


    
    val game = 
        ZIO
            .serviceWithZIO[Fighting](_.fight)
            .provide(
                ZLayer.succeed[Fighting](???) // Соберите набор слоёв, необходимых для компиялции
            )

    override def run =
        game
