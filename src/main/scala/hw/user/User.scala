package hw.user

type UserId = UserId.T
object UserId:
  opaque type T <: Int = Int
  def apply(i: Int): UserId = i

type UserName = UserName.T
object UserName:
  opaque type T <: String = String
  def apply(s: String): UserName = s

type Age = Age.T
object Age:
  opaque type T <: Byte = Byte
  val Adult: Age = 18.toByte
  def apply(v: Byte): Age = v

final case class User(id: UserId, name: UserName, age: Age, friends: Set[UserId]):
  def isAdult: Boolean = age >= Age.Adult