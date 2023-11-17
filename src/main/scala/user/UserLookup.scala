package user

import glass.{Contains, Items, Property}

enum Role:
  case Admin
  case User
case class UserId(id: Int)
case class User(id: UserId, role: Role, friends: Vector[UserId])

/**
 * 1. Создайте в объекте-компаньоне требуемые оптики для работы с данными внутри User
 */
object UserLookup:
  /**
   * Реализуйте оптику, фокусирующуюся на id внутри User
   */
  val idLens: Contains[User, Int] = ???

  /**
   * Реализуйте оптику, фокусирующуюся на роли Admin у User
   */
  val adminOptional: Property[User, Role.Admin.type] = ???

  /**
   * Реализуйте оптику, фокусирующуюся на друзьях у User
   */
  val friendsTraversal: Items[User, UserId] = ???
