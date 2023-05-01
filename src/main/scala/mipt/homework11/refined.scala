package mipt.homework11

import mipt.utils.Homeworks._

object refined extends App {

  /**
     Информацию по библиотеке refined можно найти здесь - https://github.com/fthomas/refined
  **/
  /**
    *   C помощью библиотеки 'refined' создать трейт RefinedNewType:
    *   1. Он должен иметь один тайп параметр - наш фактический тип, например A
    *   2. Он должен иметь path dependent абстрактный тип Valid
    *   3. Он должен иметь path dependent алиас Type выраженный через Refined, A, Valid
    *   4. Он должен иметь реализованный метод apply, принимающий значение фактического типа
    *      и возвращающий Either[String, Type]
    *
    */
  trait RefinedNewType

  /**
    *   C помощью библиотеки 'refined' cоздать трейт RefinedCollectionNewType:
    *   1. Он должен иметь один тайп параметр - наш фактический тип коллекции, например C
    *   2. Он должен иметь path dependent абстрактный тип Valid
    *   3. Он должен иметь path dependent алиас Type выраженный через Refined, C, Valid
    *   4. Он должен иметь реализованный метод apply, принимающий значение фактического типа
    *      и возвращающий Either[String, Type]
    *
    */
  trait RefinedCollectionNewType

  /**
    *  С помощью RefinedNewType, RefinedCollectionNewType и библиотеки refined реализовать следующие типы
    */
  task"Ранг может принимать целочисленные значения от 0 до 100 включительно"
  type Rank

  task"OptionalTrue принимает 2 значения, либо None, либо Some(true)"
  type OptionalTrue

  task"Непустой список"
  type NonEmptyList

  task"Числовое множество, содержащее в себе 0"
  type SetWithZero

  task"IPv4 или IPv6 адрес"
  type IpAddress

  task"Российский мобильный номер (с международным кодом +7)"
  type RussianMobilePhone

  case class Rational(r: Double, i: Double)

  task"Вывести 0 из типа Rational"
  type Zero

  task"Вывести тип мнимой части из типа Rational"
  type Imaginary

  task"Вывести тип действительной части из типа Rational"
  type Real

}
