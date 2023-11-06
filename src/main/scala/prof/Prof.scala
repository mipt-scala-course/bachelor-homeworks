//> using dep io.higherkindness::droste-core:0.9.0
//> using dep io.circe::circe-core:0.14.6
//> using dep org.typelevel::kittens:3.1.0
package prof

import io.circe.JsonObject
import higherkindness.droste.{Algebra, GAlgebra, GCoalgebraM, CoalgebraM}
import java.util.UUID
import io.circe.{Json, Encoder, Decoder, JsonObject, HCursor}
import io.circe.syntax.*
import higherkindness.droste.data.Fix
import higherkindness.droste.scheme.{cata, anaM}
import cats.derived.*
import cats.syntax.functor.*
import cats.{Functor, Traverse, Monad}

/**
 * В этом задании требуется реализовать кодеки и другие функции для работы с данными, представленными через фикспоинты.
 * Для работы с фикспоинтами в этом задании используйте библиотеку `droste`:
 *   https://github.com/higherkindness/droste
 *
 * Для решения домашнего задания пригодятся следующие инструменты из `higherkindness.droste`:
 *   1) `data.Fix` - реализация фикспоинта (в отличие от кода в лекции без лишнего оверхеда)
 *
 *   2) `Algebra` - алгебра
 *      `Algebra[F, A] ~= F[A] => A `
 *   3) `GAlgebra` - обобщенная алгебра
 *      `GAlgebra[F, S, A] ~= F[S] => A`
 *       Нужна в задании только для создания инстансов `Algebra`, которая выражена через нее:
 *      `type Algebra[F[_], A] = GAlgebra[F, A, A]`
 *   4) `scheme.cata` - катаморфизм
 *
 *   3) `CoalgebraM` - коалгебра с эффектом
 *      `CoalgebraM[M[_], F[_], A]` ~= A => M[F[A]]
 *   4) `GCoalgebraM` - обощенная коалгебра с эффектом
 *      `GCoalgebraM[M[_], F[_], A, S] ~= A => M[F[S]]`
 *       Нужна в задании только для создания инстансов `CoalgebraM`, которая выражена через нее:
 *       `type CoalgebraM[M[_], F[_], A] = GCoalgebraM[M, F, A, A]`
 *   5) `scheme.anaM` - анаморфизм с эффектом
 *
 *
 * В этом задании НЕЛЬЗЯ нигде реализовывать рекурсивные методы.
 * Для реализации рекурсивных обходов/построений используйте ката и анаморфизмы.
 * Реализация не должна в явном виде использовать рекурсию.
 *
 * Для использования ката/ана-морфизмов будут нужны инстансы `cats.Functor/cats.Traverse` для `ProfF/IdProfF`.
 * Их можно автоматически вывести, используя `cats.derived.*` из библиотеки `kittens` (https://github.com/typelevel/kittens),
 * либо написать самостоятельно.
 *
 */
//----------------------------------------------------------------------------
/**
 * `Prof` - рекурсивная модель профессоров/студентов, которая выражена через фикспоинт.
 * Эта модель может использоваться, например, как входная модель при записи профессоров в базу.
 */
case class ProfF[+A](
    name: String,
    year: Int,
    students: List[A]
)

type Prof = Fix[ProfF]

/**
 * `IdProf` - рекурсивная модель профессоров/студентов с идентификаторами.
 * Эта модель может использоваться, например, как выходная модель при чтении профессоров из базы.
 */
case class IdProfF[+A](id: UUID, prof: ProfF[A])

type IdProf = Fix[IdProfF]

//----------------------------------------------------------------------------
/**
 * I. Свертки
 *
 * I.1 Реализовать метод `count` который считает число людей (всех пофессоров и студентов) в дереве.
 *
 * Ожидаемое поведение:
 *   val awodey = Fix(ProfF("Steven Awodey", 1997, Nil))
 *   val howardF = Fix(ProfF("William Howard", 1956, Nil))
 *   val macLane = Fix(ProfF("Saunders Mac Lane", 1934, awodey :: howard :: Nil))
 *   val weyl = Fix(ProfF("Hermann Weyl", 1908, macLane :: Nil))
 *   val curry = Fix(ProfF("Haskell Curry", 1930, Nil))
 *   val ackerman = Fix(ProfF("Wilhelm Ackerman", 1925, Nil))
 *   val hilbert = Fix(ProfF("David Hilbert", 1885, ackerman :: curry :: weyl :: Nil))
 *
 *   count(hilbert) // 7
 *   count(macLane) // 3
 *   count(awodey)  // 1
 */
def count(prof: Prof): Int = ???

/** I.2 Реализовать алгебру и инстанс `Encoder.AsObject` для `Prof`
 *
 * Ожидаемое поведение:
 *   val awodey: Prof = Fix(ProfF("Steven Awodey", 1997, Nil))
 *   val howard: Prof = Fix(ProfF("William Howard", 1956, Nil))
 *   val macLane: Prof = Fix(ProfF("Saunders Mac Lane", 1934, awodey :: howard :: Nil))
 *
 *   macLane.asJson
 *   // {"name":"Saunders Mac Lane","year":1934,"students":[{"name":"Steven Awodey","year":1997,"students":[]},{"name":"William Howard","year":1956,"students":[]}]}
 */
val profJsonAlg: Algebra[ProfF, JsonObject] = ???

given Encoder.AsObject[Prof] = ???

/**
 * I.3 Реализовать алгебру и инстанс `Encoder.AsObject` для `IdProf`
 *
 * Ожидаемое поведение:
 *   val awodey: IdProf = Fix(IdProfF(UUID.fromString("6d828a73-aa42-4fec-be2f-172908d77137"), ProfF("Steven Awodey", 1997, Nil)))
 *   val howard: IdProf = Fix(IdProfF(UUID.fromString("7dd600e8-506a-4e7b-ad58-de1478430e04"), ProfF("William Howard", 1956, Nil)))
 *   val macLane: IdProf = Fix(IdProfF(UUID.fromString("7dd600e8-506a-4e7b-ad58-de1478430e04"), ProfF("Saunders Mac Lane", 1934, awodey :: howard :: Nil)))
 *
 *   macLane.asJson
 *   // {"id":"7dd600e8-506a-4e7b-ad58-de1478430e04","name":"Saunders Mac Lane","year":1934,"students":[{"id":"6d828a73-aa42-4fec-be2f-172908d77137","name":"Steven Awodey","year":1997,"students":[]},{"id":"7dd600e8-506a-4e7b-ad58-de1478430e04","name":"William Howard","year":1956,"students":[]}]}
 *
 * При реализации переиспользовать алгебру `profJsonAlg`.
 */
val idProfJsonAlg: Algebra[IdProfF, JsonObject] = ???

given Encoder.AsObject[IdProf] = ???

//----------------------------------------------------------------------------
/**
 * II. Декодеры для моделей
 *
 * II. 1. Реализовать коалгебру и декодер для `Prof`.
 *
 * Декодер должен быть консистентен с энкодером.
 * Ожидаемое поведение:
 *
 *   val json = """{"name":"Saunders Mac Lane","year":1934,"students":[{"name":"Steven Awodey","year":1997,"students":[]},{"name":"William Howard","year":1956,"students":[]}]}"""
 *   io.circe.parser.parse(json).flatMap(_.as[Prof]))
 *   // Right(ProfF(Saunders Mac Lane,1934,List(ProfF(Steven Awodey,1997,List()), ProfF(William Howard,1956,List())))
 *
 * Инстанс `cats.Traverse` для `ProfF` аналогично - вывести автоматом или написать самостоятельно.
 */
val profJsonCoalg: CoalgebraM[Decoder.Result, ProfF, Json] = ???

given Decoder[Prof] = ???

/**
 * II. 2. Реализовать коалгебру и декодер для `IdProf`.
 *
 * Декодер должен быть консистентен с энкодером.
 * Ожидаемое поведение:
 *   val json = """{"id":"7dd600e8-506a-4e7b-ad58-de1478430e04","name":"Saunders Mac Lane","year":1934,"students":[{"id":"6d828a73-aa42-4fec-be2f-172908d77137","name":"Steven Awodey","year":1997,"students":[]},{"id":"7dd600e8-506a-4e7b-ad58-de1478430e04","name":"William Howard","year":1956,"students":[]}]}"""
 *   io.circe.parser.parse(json).flatMap(_.as[IdProf])
 *   // Right(IdProfF(7dd600e8-506a-4e7b-ad58-de1478430e04,ProfF(Saunders Mac Lane,1934,List(IdProfF(6d828a73-aa42-4fec-be2f-172908d77137,ProfF(Steven Awodey,1997,List())), IdProfF(7dd600e8-506a-4e7b-ad58-de1478430e04,ProfF(William Howard,1956,List()))))))
 *
 * При реализации переиспользовать коалгебру `idProfJsonAlg`.
 */
val idProfJsonCoalg: CoalgebraM[Decoder.Result, IdProfF, Json] = ???

given Decoder[IdProf] = ???

//----------------------------------------------------------------------------
/**
 * Вспомогательный класс типов для effectful генерации рандомизированных UUID для идентификаторов профессоров
 */
trait GenUUID[F[_]]:
  def randomUUID: F[UUID]

/**
 * III. Трансформации между Prof и IdProf
 *
 * III.1 IdProf => Prof
 *  Реализовать extension-метод `toProf` для `IdProf` который конвертирует его в `Prof`,
 *  убирая из модели идентификаторы профессоров.
 *
 *  Ожидаемое поведение:
 *    val awodey: IdProf = Fix(IdProfF(UUID.randomUUID, ProfF("Steven Awodey", 1997, Nil)))
 *    awodey.toProf // ProfF(Steven Awodey,1997,List())
 */
extension (prof: IdProf) def toProf: Prof = ???

/**
 * III.2 Prof => IdProf
 *  Реализовать extension-метод `genId` для `Prof` который будет создавать на его основе `ProfId`,
 *  генерируя идентификаторы профессоров.
 *
 *  Пример поведения:
 *    import cats.Id
 *    given GenUUID[Id] with
 *      def randomUUID: UUID = UUID.randomUUID()
 *
 *    val awodey: Prof = Fix(ProfF("Steven Awodey", 1997, Nil))
 *    awodey.genId[Id]
 *    // IdProfF(81d6cf30-9750-4c56-9eed-ec07c8b5548b,ProfF(Steven Awodey,1997,List()))
 */
extension (prof: Prof)
  def genId[F[_]: Monad: GenUUID]: F[IdProf] = ???
