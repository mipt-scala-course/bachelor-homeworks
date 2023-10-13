package hw.user

object UserErrors:
  case class UserAlreadyExists(name: UserName) extends Throwable
  case class UserDoesNotExists(id: UserId) extends Throwable