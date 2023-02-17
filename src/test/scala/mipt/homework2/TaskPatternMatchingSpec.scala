package mipt.homework2

import org.scalatest.flatspec.AnyFlatSpec

class TaskPatternMatchingSpec extends AnyFlatSpec {
  import task1_pattern_matching._

  "Animals" should "be detected precisely" in {
    assert(matching(oldFatCat) == "Old fat cat")
    assert(matching(oldSlimCat) == "Old slim cat")
    assert(matching(youngFatCat) == "Young fat cat")
    assert(matching(smallKitten) == "Small kitten")
    assert(matching(forestWolf) == "Forest wolf")
    assert(matching(polarWolf) == "Polar wolf")
    assert(matching(prettyFox) == "Pretty fox")
    assert(matching(fish) == "Nemo")
  }

}
