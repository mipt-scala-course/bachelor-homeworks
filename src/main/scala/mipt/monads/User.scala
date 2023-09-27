package mipt.monads

import math.Ordering.Implicits.infixOrderingOps

opaque type UserId <: Int = Int
object UserId:
  def apply(i: Int): UserId = i

opaque type UserName <: String = String

object UserName:
  def apply(s: String): UserName = s

opaque type Age <: Byte = Byte

object Age:
  val Adult: Age = 18
  def apply(v: Byte): Age = v

final case class User(id: UserId, name: UserName, age: Age, friends: Set[UserId]):
  def isAdult: Boolean = age >= Age.Adult
