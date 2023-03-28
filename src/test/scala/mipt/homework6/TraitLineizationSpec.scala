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

    val traitDefFin1 = TraitDef(
      parents = List(
        traitDefB,
        traitDefC,
        traitDefA
      ),
      methods = Map.empty
    )
    val traitDefFin2 = TraitDef(
      parents = List(
        traitDefA,
        traitDefB
      ),
      methods = Map.empty
    )

    resolveMethod(traitDefFin1, "foo") shouldBe Some("println kek")
    resolveMethod(traitDefFin2, "foo") shouldBe Some("println lol")

  }

}
