package mipt.homework10

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class PrompterSpec extends AnyFunSuite with Matchers {
  import Homework.PrompterInterpreter
  import Homework.PrompterInterpreter._
  import Homework.PrompterPrograms._

  test("Реплики Джульетты") {
    stringPrompterInterpreter.prompt(prompterProgramInit) shouldBe """suggest: "Last sleep" and give the Special potion"""
    PrompterInterpreter[String].prompt(prompterProgramFinish) shouldBe """suggest: "Romeo is dead" and give the Stiletto"""
  }

  test("Реплики Ромео") {
    stringPrompterInterpreter.prompt(prompterProgramNext) shouldBe """suggest: "Juliet is dead" and give the Poison flask"""
  }

}
