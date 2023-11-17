package user

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class UserLookupSpec extends AnyFlatSpec, Matchers:
  val user: User = User(UserId(42), Role.Admin, Vector(UserId(1), UserId(2), UserId(3)))

  it should "get id" in {
    UserLookup.idLens.get(user) shouldBe 42
  }

  it should "update id" in {
    UserLookup.idLens.set(user, 2023) shouldBe User(UserId(2023), Role.Admin, Vector(UserId(1), UserId(2), UserId(3)))
  }

  it should "narrow role right" in {
    UserLookup.adminOptional.narrow(user) shouldBe Right(Role.Admin)
  }

  it should "narrow role left" in {
    val notAdmin = User(UserId(1), Role.User, Vector(UserId(42)))
    UserLookup.adminOptional.narrow(notAdmin) shouldBe Left(notAdmin)
  }

  it should "update friends" in {
    UserLookup.friendsTraversal.update(user, id => UserId(id.id + 1)) shouldBe
      User(UserId(42), Role.Admin, Vector(UserId(2), UserId(3), UserId(4)))
  }
