package mipt.homework6

import TraitLineization._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
class TraitLineizationSpec extends AnyFlatSpec with Matchers {

  "resolveMethod" should "correctly resolve method for multi-trait extension chain" in {

    val traitDefA = TraitDef(
      parents = List.empty,
      methods = Map("foo" -> "println bar")
    )

    val traitDefB = TraitDef(
      parents = List(traitDefA),
      methods = Map("foo" -> "println lol")
    )

    val traitDefC = TraitDef(
      parents = List(traitDefA),
      methods = Map("foo" -> "println kek")
    )
    val traitDef = TraitDef(
      parents = List(
        traitDefB,
        traitDefC,
        traitDefA
      ),
      methods = Map("bar" -> "bar")
    )

    resolveMethod(traitDef, "foo") shouldBe Some("println lol")
    resolveMethod(traitDef, "bar") shouldBe Some("bar")

  }

}
