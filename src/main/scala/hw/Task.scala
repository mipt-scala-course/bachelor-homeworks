package hw

import io.circe.{Json, Printer}
import io.circe.Encoder

import java.time.{LocalDate, LocalDateTime}
import scala.util.NotGiven
import scala.util.matching.Regex

/** *
  *   I. Новые типы
  *
  * 1) Реализовать новый тип Login. Логин должен содержать латинские буквы (маленькие/большие), цифры и нижнее
  * подчеркивание. Логин не может начинаться c нижнего подчеркивания или цифры. Тип Login должен быть подтипом String.
  *
  * 2) Реализовать новый тип Name. Имя должно быть непустым и не может начинаться с пробела. Тип Name должен быть
  * подтипом String.
  *
  * II. Классы типов: Loggable
  *
  * Loggable[A] - класс типов, описывающий как логгировать в виде Json значения типа A. Данные с чувствительной
  * информацией должны маскироваться. Тип Json - модель для описания джисонов из библиотеки circe. Для выполнения
  * данного задания также понадобится класс типов Encoder (Encoder.AsObject), который описывает как значение превратить
  * в Json (например, для последующей отправки по сети). Разница между Encoder и Loggable - работа с чувствительными
  * данными, Encoder - не маскирует их и оставляет как есть.
  *
  * 1) Реализовать метод contramap в классе типов Loggable. Добавить инстанс Loggable[String].
  *
  * 2) Реализовать инстанс Loggable для Name, который маскирует все символы имени после 6-го включительно.
  *
  * 3) Реализовать инстанс Loggable для JwtToken. Поле token - чувствительная информация, должно логироваться как "***".
  *
  * 4) Используя инстансы Loggable[JwtToken], Loggable[Name] реализовать инстанс Loggable[User].
  *
  * 5) Добавить метод log, который логирует модель (печатает в консоль) в соответствии с ее инстансом Loggable, добавляя
  * поля message (единственный параметр метода log) и timestamp (текущее время). Метод log должен быть доступен (быть
  * может, с дополнительным импортом) для любого типа, для которго есть инстанс Loggable. Реализовать через конструкцию
  * extention.
  *
  * val user: User = ... user.log("user signed in")
  *
  * Для пользователя с логином vasya и именем Vasiliy должно распечатать что-то вроде: {"message":"user signed
  * in","timestamp":"2023-09-06T12:33:10.784230","context":{"login":"vasya","name":"Vasil**","token":{"token":"***","exp":1693929522}}}
  *
  * timestamp - текущее время в поле context - информация о том значении, которое логируем (в данном случае - о
  * пользователе)
  *
  * Печатать вывод (json) без пробелов и переводов строк.
  *
  * III. Классы типов: Sensitive
  *
  * Sensitive[A] - класс типов, показывающий что данные содержат чувствительную информацю. Sensitive - не содержит
  * методов, наличие инстанса Sensitive для типа A - показывает, что A содержит чувствительные данные. Отсутствие
  * инстанса Sensitive у A - показывает, что A не содержит чувствительные данные.
  *
  * 1) Используя синтаксис derives (и реализуя необходимый для этого инструментал в Sensitive) "пометить" данные
  * JwtToken, User, Name как чувствительные.
  *
  * 2) Написать универсальный метод, который выводит инстанс Loggable для любой модели, НЕ содержащей чувствительную
  * информацию, имеющей инстанс Encoder. Надо, чтобы такой код работал без дополнительных инстансов:
  *
  * import Loggable.given // или import Loggable.* в зависимости от вашей реализации log
  *
  * case class Custom(foo: String, bar: Int) derives Encoder.AsObject Custom("foo", 42).log("custom event")
  */
type Login //= ???
object Login:
  def apply(str: String): Either[String, Login] = ???

type Name //= ???
object Name:
  def apply(str: String): Either[String, Name] = ???

trait Loggable[A]:
  def jsonLog(a: A): Json

  def contramap[B](f: B => A): Loggable[B] = ???

object Loggable

trait Sensitive[A]

case class JwtToken(token: String, exp: Long)

case class User(
    login: Login,
    name: Name,
    token: JwtToken
)
