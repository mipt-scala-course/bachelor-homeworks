package hw.user

import cats.mtl.Ask
import cats.{Applicative, MonadError, MonadThrow}
import hw.user.UserErrors.{UserAlreadyExists, UserDoesNotExists}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.Random

object TestsData:
  var lastConfig: Either[Unit, Config] = Left(())
  val sampleUsers: List[User] = List(
    User(UserId(0), UserName("Subject #0"), Age(42), Set.empty),
    User(UserId(1), UserName("Subject #1"), Age(41), Set.empty),
    User(UserId(2), UserName("Subject #2"), Age(40), Set.empty),
    User(UserId(3), UserName("Subject #3"), Age(39), Set.empty),
    User(UserId(4), UserName("Subject #4"), Age(38), Set.empty)
  )

  def dao(users: List[User]) = new UserRepositoryDao:
    var usersList = users.toSet

    override def findAll(config: Config): List[User] =
      lastConfig = Right(config)
      usersList.toList

    override def create(name: UserName, age: Age, friends: Set[UserId])(config: Config): Either[UserAlreadyExists, User] =
      lastConfig = Right(config)
      if (usersList.exists(_.name == name))
        Left(UserAlreadyExists(name))
      else
        val user = User(UserId(usersList.size), name, age, friends)
        usersList += user
        Right(user)

    override def delete(userId: UserId)(config: Config): Either[UserDoesNotExists, Unit] =
      lastConfig = Right(config)
      if (usersList.exists(_.id == userId))
        usersList -= usersList.find(_.id == userId).get
        Right(())
      else
        Left(UserDoesNotExists(userId))

    override def update(user: User)(config: Config): Either[UserDoesNotExists, Unit] =
      lastConfig = Right(config)
      if (usersList.exists(_.id == user.id))
        usersList -= usersList.find(_.id == user.id).get
        usersList += user
        Right(())
      else
        Left(UserDoesNotExists(user.id))

  type M = [A] =>> Config => Either[Throwable, A]

  object M:
    given MonadThrow[M] = new MonadError[M, Throwable]:
      override def pure[A](x: A): M[A] = _ => Right(x)
      override def flatMap[A, B](fa: M[A])(f: A => M[B]): M[B] = c => fa(c).flatMap(a => f(a)(c))
      override def tailRecM[A, B](a: A)(f: A => M[Either[A, B]]): M[B] =
        c => f(a)(c) match
          case Left(e)         => Left(e)
          case Right(Left(a))  => tailRecM(a)(f)(c)
          case Right(Right(b)) => Right(b)
      override def raiseError[A](e: Throwable): M[A] = _ => Left(e)
      override def handleErrorWith[A](fa: M[A])(f: Throwable => M[A]): M[A] = c => fa(c) match
        case Left(e)  => f(e)(c)
        case Right(a) => Right(a)

    given Ask[M, Config] = new Ask[M, Config]:
      override def applicative: Applicative[M] = MonadThrow[M]

      override def ask[E2 >: Config]: M[E2] = c => Right(c)

class Tests extends AnyFlatSpec with Matchers:
  import TestsData.*
  import M.given

  it should "correct findAll config propagation" in {
    val repository = UserRepositoryDao[M](dao(List.empty))
    val config = Config(42)
    repository.findAll(config)
    lastConfig shouldBe Right(Config(42))
  }

  it should "correct create config propagation" in {
    val repository = UserRepositoryDao[M](dao(List.empty))
    val config = Config(43)
    repository.create(UserName("Subject #0"), Age(0), Set.empty)(config)
    lastConfig shouldBe Right(Config(43))
  }

  it should "correct delete config propagation" in {
    val repository = UserRepositoryDao[M](dao(List.empty))
    val config = Config(44)
    repository.delete(UserId(0))(config)
    lastConfig shouldBe Right(Config(44))
  }

  it should "correct update config propagation" in {
    val repository = UserRepositoryDao[M](dao(List.empty))
    val config = Config(45)
    repository.update(User(UserId(0), UserName("Subject #0"), Age(0), Set.empty))(config)
    lastConfig shouldBe Right(Config(45))
  }

  it should "correct return all users" in {
    val repository = UserRepositoryDao[M](dao(sampleUsers))
    val config = Config(46)
    repository.findAll(config) shouldBe Right(sampleUsers.toSet.toList)
  }

  it should "correct create new user" in {
    val repository = UserRepositoryDao[M](dao(sampleUsers))
    val config = Config(46)
    repository.create(UserName("New subject"), Age(0), Set.empty)(config) shouldBe Right(
      User(UserId(5), UserName("New subject"), Age(0), Set.empty)
    )
  }

  it should "not create user more than once" in {
    val repository = UserRepositoryDao[M](dao(sampleUsers))
    val config = Config(46)
    repository.create(UserName("Subject #2"), Age(0), Set.empty)(config) shouldBe a [Left[Throwable, User]]
  }

  it should "delete existing user" in {
    val repository = UserRepositoryDao[M](dao(sampleUsers))
    val config = Config(46)
    repository.delete(UserId(2))(config) shouldBe Right(())
  }

  it should "not delete nonexistent user" in {
    val repository = UserRepositoryDao[M](dao(sampleUsers))
    val config = Config(46)
    repository.delete(UserId(42))(config) shouldBe a [Left[Throwable, Unit]]
  }

  it should "update existing user" in {
    val repository = UserRepositoryDao[M](dao(sampleUsers))
    val config = Config(46)
    repository.update(User(UserId(2), UserName("Subject #2"), Age(42), Set.empty))(config) shouldBe Right(())
  }

  it should "not update nonexistent user" in {
    val repository = UserRepositoryDao[M](dao(sampleUsers))
    val config = Config(46)
    repository.update(User(UserId(22), UserName("Subject #22"), Age(42), Set.empty))(config) shouldBe a [Left[Throwable, Unit]]
  }
