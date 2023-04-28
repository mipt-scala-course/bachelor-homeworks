package mipt.homework10

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers
import org.mockito.IdiomaticMockito

class ActorPlaySpec extends AnyFunSuite with Matchers with IdiomaticMockito {
  import Auxiliary.Odeum
  import Homework.Prompter
  import Homework.PrompterInterpreter
  import Homework.ScenePlay
  import Homework.ScenePlay._

  trait OdeumChannel {
    def sink(value: String): Unit
  }

  test("Реплики Джульетты") {
    val theaterGoers = mock[OdeumChannel]
    implicit val odeum: Odeum[String] =
      Odeum(
        see = theaterGoers.sink,
        listen = theaterGoers.sink,
      )

    val firstJulietPlay =
      Homework.SceneplayPrograms.julietInitPlay(
        Homework.PrompterPrograms.prompterProgramInit
      )

    val lastJulietPlay =
      Homework.SceneplayPrograms.julietFinishPlay(
        Homework.PrompterPrograms.prompterProgramFinish
      )

    val mistakenJulietPlay =
      Homework.SceneplayPrograms.julietFinishPlay(
        Homework.PrompterPrograms.prompterProgramNext
      )

    firstJulietPlay.action()
    theaterGoers.sink("I will try to cheat!") was called
    theaterGoers.sink("Juliet drink the special potion") was called

    lastJulietPlay.action()
    theaterGoers.sink("No reason to live!") was called
    theaterGoers.sink("Juliet pierces her heart with a stiletto") was called

    mistakenJulietPlay.action()
    theaterGoers.sink("silent in confusion") was called

  }

  test("Реплики Ромео") {
    val theaterGoers = mock[OdeumChannel]
    implicit val odeum: Odeum[String] =
      Odeum(
        see = theaterGoers.sink,
        listen = theaterGoers.sink,
      )

    val correctRomeoPlay =
      Homework.SceneplayPrograms.romeoPlay(
        Homework.PrompterPrograms.prompterProgramNext
      )

    val mistakenRomeoPlay =
      Homework.SceneplayPrograms.romeoPlay(
        Homework.PrompterPrograms.prompterProgramFinish
      )

    correctRomeoPlay.action()
    theaterGoers.sink("Oh, no!") was called
    theaterGoers.sink("Romeo drink from Poison flask") was called

    mistakenRomeoPlay.action()
    theaterGoers.sink("silent in confusion") was called
  }

}
