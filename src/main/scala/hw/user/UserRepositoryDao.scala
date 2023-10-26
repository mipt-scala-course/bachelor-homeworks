package hw.user

import cats.MonadThrow
import cats.mtl.Ask
import cats.syntax.applicativeError.*
import cats.syntax.applicative.*
import cats.syntax.flatMap.*
import cats.syntax.functor.*
import hw.user.UserErrors.*

case class Config(chunkSize: Int)

trait UserRepositoryDao:
  def findAll(config: Config): List[User]
  def create(name: UserName, age: Age, friends: Set[UserId] = Set.empty)(
      config: Config
  ): Either[UserAlreadyExists, User]
  def delete(userId: UserId)(config: Config): Either[UserDoesNotExists, Unit]
  def update(user: User)(config: Config): Either[UserDoesNotExists, Unit]

trait UserRepository[F[_]]:
  def findAll: F[List[User]]
  def create(name: UserName, age: Age, friends: Set[UserId] = Set.empty): F[User]
  def delete(userId: UserId): F[Unit]
  def update(user: User): F[Unit]

/** IV. Обёртка для работы с базой данных
  *
  * В последнем задании рассмотрим более практичный пример: пусть есть интерфейс UserRepositoryDao, который обеспечивает
  * работу с базой данных, обновляя данные о пользователях (модель пользователей и операции, проводимые с ними,
  * описываются кодом в данном файле и в User.scala, UserErrors.scala). От вас требуется сделать обёртку над данным
  * интерфейсом, чтобы его "грязные" функции можно было бы использовать в коде в функциональном стиле. При написании
  * используйте интерфейсы MonadThrow и Ask - такой подход часто называется Tagless final
  */
object UserRepositoryDao:
  def apply[F[_]: MonadThrow](dao: UserRepositoryDao)(using Ask[F, Config]): UserRepository[F] = new UserRepository[F]:
    /** IV.1) Фукнция findAll
      *
      * Для данной функции требуется, используя интерфейс Ask из библиотеки Cats (полностью аналогичный варианту из
      * предыдущего задания), получить конфиг, хранящийся в монаде F, и вернуть значение, обёрнутое в монаду
      */
    override def findAll: F[List[User]] = ???

    /** IV.2) Фукнция create
      *
      * Для этой функции аналогично нужно получить конфиг, а так же дополнительно обработать возможные ошибки при помощи
      * интерфейса MonadThrow (примеры использования можно найти на сайте cats)
      */
    override def create(name: UserName, age: Age, friends: Set[UserId]): F[User] = ???

    /** IV.3) Функция delete
      *
      * Данная функция аналогично требует получения конфига и обработки ошибки
      */
    override def delete(userId: UserId): F[Unit] = ???

    /** IV.4) Функция update
      *
      * Для последней функции задание аналогично
      */
    override def update(user: User): F[Unit] = ???
