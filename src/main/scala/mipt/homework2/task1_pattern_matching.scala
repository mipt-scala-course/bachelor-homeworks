package mipt.homework2

import mipt.utils.Homeworks._

object task1_pattern_matching {

  type Age    = Byte
  type Weight = Double
  type Name   = String
  type Angry  = Boolean

  sealed trait Animal

  task"Добавьте классы extends Animal такие, что бы могли быть определены закоментированные переменные ниже. Используйте тип Age для возраста, Weight для массы тела и Name для клички животного."
  case class Cat(age: Age, mass: Weight) extends Animal

  val oldFatCat   = Cat(10, 12.0)
  val oldSlimCat  = Cat(10, 4.0)
  val youngFatCat = Cat(3, 12.0)
  val smallKitten = Cat(1, 2.5)
  val forestWolf  = ??? //DogLike(40.0, true)
  val polarWolf   = ??? //DogLike(40.0, false)
  val prettyFox   = ??? //DogLike(7.5, false)
  val fish        = ??? //Fish(1, "Nemo")

  val old  = (cat: Cat) => cat.age >= 10
  val slim = (cat: Cat) => cat.mass <= 7

  task"Заполните пробелы в pattern matching. Используйте для ограждения функции old и slim в случае кошек."
  def matching(animal: Animal): String = ???
  // animal match {
  //     case ??? => "Old fat cat"
  //     case ??? => "Pretty fox"
  //     case ??? => "Old slim cat"
  //     case ??? => "Forest wolf"
  //     case ??? => "Nemo"
  //     case ??? => "Small kitten"
  //     case ??? => "Young fat cat"
  //     case ??? => "Polar wolf"
  // }

}
