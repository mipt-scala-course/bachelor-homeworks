package mipt.homework10

import mipt.utils.Homeworks._
import Auxiliary._

object Homework {

  // Prompter model
  sealed trait Prompter

  object Prompter {
    // Prompter model
    case class Whisper(suggestion: String)              extends Prompter
    case class Submit(props: String)                    extends Prompter
    case class Combo(before: Prompter, after: Prompter) extends Prompter

    // Prompter constructors
    def whisper(suggestion: String): Prompter =
      task"""Реализуйте конструктор""" (1, 1)

    def submit(props: String): Prompter =
      task"""Реализуйте конструктор""" (1, 2)

    // Prompter operators
    def combined(some: Prompter, other: Prompter) =
      task"""Реализуйте оператор""" (1, 3)
  }
  import Prompter._

  // Prompter interpreter
  trait PrompterInterpreter[A] {
    def prompt: Prompter => A
  }

  object PrompterInterpreter {
    implicit val stringPrompterInterpreter: PrompterInterpreter[String] =
      new PrompterInterpreter[String] {
        override def prompt: Prompter => String =
          task"""Реализуйте интерпретатор так, чтобы:
                            1. В случае подсказки возвращалась строка вида: suggest: "подсказка"
                            2. В случае передачи реквизита строка вида: give the реквизит
                            3. В случае комбинированного действия: первое действие and второе действие
                        """ (1, 4)
      }

    // Prompter Interpreter summoiner
    def apply[A](implicit prompterInterpreter: PrompterInterpreter[A]): PrompterInterpreter[A] =
      prompterInterpreter
  }

  // Actor domain model
  final case class ScenePlay[A](action: () => A)

  object ScenePlay {
    // Actor constructors
    def ask[A: PrompterInterpreter](prompter: Prompter): ScenePlay[A] =
      task"""Реализуйте конструктор, запрос подсказки у суфлёра""" (1, 5)

    def act[A: Odeum](action: String): ScenePlay[Unit] =
      task"""Реализуйте конструктор, действие актёра на сцене""" (1, 6)

    def say[A: Odeum](speech: String): ScenePlay[Unit] =
      task"""Реализуйте конструктор, актёрская реплика""" (1, 7)

    // Actor operators
    def combine[A: Semigroup](earlier: ScenePlay[A], later: ScenePlay[A]): ScenePlay[A] =
      task"""Реализуйте оператор""" (1, 8)

    def select[A, B](root: ScenePlay[A], predicate: A => Boolean)(ifTrue: ScenePlay[B], ifFalse: ScenePlay[B]) =
      task"""Реализуйте оператор""" (1, 9)

  }
  import ScenePlay._

  // ---
  // Programs
  // ---

  object PrompterPrograms {
    val prompterProgramInit: Prompter =
      combined(whisper("Last sleep"), submit("Special potion"))

    val prompterProgramNext: Prompter =
      combined(whisper("Juliet is dead"), submit("Poison flask"))

    val prompterProgramFinish: Prompter =
      combined(whisper("Romeo is dead"), submit("Stiletto"))
  }
  import PrompterPrograms._

  object SceneplayPrograms {
    def julietInitPlay(prompter: Prompter)(implicit theaterGoers: Odeum[String]) =
      select(
        ask(prompter),
        (sugg: String) => sugg == """suggest: "Last sleep" and give the Special potion"""
      )(
        combine(say("I will try to cheat!"), act("Juliet drink the special potion")),
        act("silent in confusion")
      )

    def romeoPlay(prompter: Prompter)(implicit theaterGoers: Odeum[String]) =
      select(
        ask(prompter),
        (sugg: String) => sugg == """suggest: "Juliet is dead" and give the Poison flask"""
      )(
        combine(say("Oh, no!"), act("Romeo drink from Poison flask")),
        act("silent in confusion")
      )

    def julietFinishPlay(prompter: Prompter)(implicit theaterGoers: Odeum[String]) =
      select(
        ask(prompter),
        (sugg: String) => sugg == """suggest: "Romeo is dead" and give the Stiletto"""
      )(
        combine(say("No reason to live!"), act("Juliet pierces her heart with a stiletto")),
        act("silent in confusion")
      )
  }
  import SceneplayPrograms._

  def finalProgram(implicit theaterGoers: Odeum[String]) =
    combine(
      combine(
        julietInitPlay(prompterProgramInit),
        romeoPlay(prompterProgramNext)
      ),
      julietFinishPlay(prompterProgramFinish)
    )

  // ---
  // Program evaluation
  // ---
  private val theaterGoers: Odeum[String] =
    Odeum(
      see = actorPlay => println(s"theater-goers see: <$actorPlay>"),
      listen = actorSpeech => println(s"theater-goers listen: [$actorSpeech]"),
    )

  finalProgram(theaterGoers).action()

}
