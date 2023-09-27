package mipt.monads

import cats.MonadThrow
import cats.data.OptionT
import cats.syntax.flatMap.toFlatMapOps
import cats.syntax.functor.toFunctorOps
import cats.syntax.traverse.toTraverseOps

import cats.syntax.applicativeError
import scala.concurrent.Future
import scala.util.control.NoStackTrace

trait UserRepository[F[_]]:
  def findAll: F[List[User]]
  def create(name: UserName, age: Age, friends: Set[UserId] = Set.empty): F[User]
  def delete(userId: UserId): F[Unit]
  def update(user: User): F[Unit]

object UserRepository:
  case class UserNotFoundError(id: UserId) extends Throwable

  type Op[F[_], T] = UserRepository[F] => F[T]

  given [F[_]: MonadThrow]: MonadThrow[Op[F, *]] = ???

  object Operations:
    def findAll[F[_]]: Op[F, List[User]] =
      _.findAll

    def create[F[_]](name: UserName, age: Age, friends: Set[UserId] = Set.empty): Op[F, User] =
      _.create(name, age, friends)

    def delete[F[_]](userId: UserId): Op[F, Unit] = _.delete(userId)

    def update[F[_]](user: User): Op[F, Unit] = _.update(user)

    /** реализуйте композитные методы, используя базовые выше
      *
      * для работы с ошибками можно использовать синтаксис из cats.syntax.applicativeError val err: Op[User] =
      * UserNotFoundError(UserId(1)).raiseError[Op, User]
      */

    /** Метод опционального поиска пользователя */
    def findMaybe[F[_]](userId: UserId)(using me: MonadThrow[F]): Op[F, Option[User]] =
      ???

    /** Метод поиска пользователя. Если пользователь не найден, должна генерироваться ошибка UserNotFound */
    def find[F[_]](userId: UserId)(using me: MonadThrow[F]): Op[F, User] =
      ???

    /** Метод добавления друга к пользователю. */
    def addFriend[F[_]](currentUserId: UserId, friendId: UserId)(using
        me: MonadThrow[F]
    ): Op[F, User] =
      ???

    /** Метод удаления друга у пользователя */
    def deleteFriend[F[_]](currentUserId: UserId, friendId: UserId)(using
        me: MonadThrow[F]
    ): Op[F, User] =
      ???

    /** Метод получения всех друзей пользователя */
    def getUserFriends[F[_]](userId: UserId)(using
        me: MonadThrow[F]
    ): Op[F, List[User]] =
      ???

    /** Метод получения пользователей, у которых в друзьях только взрослые пользователи */
    def getUsersWithAdultOnlyFriends[F[_]]()(using
        me: MonadThrow[F]
    ): Op[F, List[User]] =
      ???

    /** Метод удаления всех молодых пользователей */
    def deleteAllJuniorUsers[F[_]]()(using
        me: MonadThrow[F]
    ): Op[F, Unit] =
      ???

    /** Метод создания сообщества, где все являются друзьями друг для друга. На вход подается список атрибутов
      * пользователей из сообщества
      */
    def createCommunity[F[_]](community: List[(UserName, Age)])(using
        me: MonadThrow[F]
    ): Op[F, List[User]] =
      ???
