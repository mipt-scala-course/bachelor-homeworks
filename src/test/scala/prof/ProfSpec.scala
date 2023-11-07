package prof

import io.circe.{Json, Printer}
import io.circe.syntax.*
import io.circe.parser.parse
import higherkindness.droste.data.Fix
import cats.syntax.profunctor
import java.util.UUID
import cats.Id

class Tests extends munit.FunSuite:
  def profF(name: String, year: Int): [A] => List[A] => ProfF[A] =
    [A] => (s: List[A]) => ProfF(name, year, s)

  val awodeyUUID       = UUID.randomUUID
  val awodeyF          = ProfF("Steven Awodey", 1997, Nil)
  val awodey: Prof     = Fix(awodeyF)
  val awodeyId: IdProf = Fix(IdProfF(awodeyUUID, awodeyF))

  val howardUUID       = UUID.randomUUID
  val howardF          = ProfF("William Howard", 1956, Nil)
  val howard: Prof     = Fix(howardF)
  val howardId: IdProf = Fix(IdProfF(howardUUID, howardF))

  val macLaneUUID       = UUID.randomUUID
  val macLaneF          = profF("Saunders Mac Lane", 1934)
  val macLane: Prof     = Fix(macLaneF(awodey :: howard :: Nil))
  val macLaneId: IdProf = Fix(IdProfF(macLaneUUID, macLaneF(awodeyId :: howardId :: Nil)))

  val weylUUID       = UUID.randomUUID
  val weylF          = profF("Hermann Weyl", 1908)
  val weyl: Prof     = Fix(weylF(macLane :: Nil))
  val weylId: IdProf = Fix(IdProfF(weylUUID, weylF(macLaneId :: Nil)))

  val curryUUID       = UUID.randomUUID
  val curryF          = ProfF("Haskell Curry", 1930, Nil)
  val curry: Prof     = Fix(curryF)
  val curryId: IdProf = Fix(IdProfF(curryUUID, curryF))

  val ackermanUUID       = UUID.randomUUID
  val ackermanF          = ProfF("Wilhelm Ackerman", 1925, Nil)
  val ackerman: Prof     = Fix(ackermanF)
  val ackermanId: IdProf = Fix(IdProfF(ackermanUUID, ackermanF))

  val hilbertUUID       = UUID.randomUUID
  val hilbertF          = profF("David Hilbert", 1885)
  val hilbert: Prof     = Fix(hilbertF(ackerman :: curry :: weyl :: Nil))
  val hilbertId: IdProf = Fix(IdProfF(hilbertUUID, hilbertF(ackermanId :: curryId :: weylId :: Nil)))

  val profs: List[(Prof, IdProf)] =
    (awodey, awodeyId) :: (howard, howardId) :: (macLane, macLaneId) :: (weyl, weylId) ::
      (curry, curryId) :: (ackerman, ackermanId) :: (hilbert, hilbertId) :: Nil

  val profJson = """|{
                    |  "name" : "David Hilbert",
                    |  "year" : 1885,
                    |  "students" : [
                    |    {
                    |      "name" : "Wilhelm Ackerman",
                    |      "year" : 1925,
                    |      "students" : [
                    |      ]
                    |    },
                    |    {
                    |      "name" : "Haskell Curry",
                    |      "year" : 1930,
                    |      "students" : [
                    |      ]
                    |    },
                    |    {
                    |      "name" : "Hermann Weyl",
                    |      "year" : 1908,
                    |      "students" : [
                    |        {
                    |          "name" : "Saunders Mac Lane",
                    |          "year" : 1934,
                    |          "students" : [
                    |            {
                    |              "name" : "Steven Awodey",
                    |              "year" : 1997,
                    |              "students" : [
                    |              ]
                    |            },
                    |            {
                    |              "name" : "William Howard",
                    |              "year" : 1956,
                    |              "students" : [
                    |              ]
                    |            }
                    |          ]
                    |        }
                    |      ]
                    |    }
                    |  ]
                    |}
                    |""".stripMargin

  val idProfJson = s"""|{
                       |  "id" : "$hilbertUUID",
                       |  "name" : "David Hilbert",
                       |  "year" : 1885,
                       |  "students" : [
                       |    {
                       |      "id" : "$ackermanUUID",
                       |      "name" : "Wilhelm Ackerman",
                       |      "year" : 1925,
                       |      "students" : [
                       |      ]
                       |    },
                       |    {
                       |      "id" : "$curryUUID",
                       |      "name" : "Haskell Curry",
                       |      "year" : 1930,
                       |      "students" : [
                       |      ]
                       |    },
                       |    {
                       |      "id" : "$weylUUID",
                       |      "name" : "Hermann Weyl",
                       |      "year" : 1908,
                       |      "students" : [
                       |        {
                       |          "id" : "$macLaneUUID",
                       |          "name" : "Saunders Mac Lane",
                       |          "year" : 1934,
                       |          "students" : [
                       |            {
                       |              "id" : "$awodeyUUID",
                       |              "name" : "Steven Awodey",
                       |              "year" : 1997,
                       |              "students" : [
                       |              ]
                       |            },
                       |            {
                       |              "id" : "$howardUUID",
                       |              "name" : "William Howard",
                       |              "year" : 1956,
                       |              "students" : [
                       |              ]
                       |            }
                       |          ]
                       |        }
                       |      ]
                       |    }
                       |  ]
                       |}
                       |""".stripMargin

  test("I.1 Count professors/students"):
    assertEquals(count(hilbert), 7)
    assertEquals(count(macLane), 3)
    assertEquals(count(awodey), 1)

  test("I.2 Encoder for Prof"):
    assertEquals(parse(profJson), Right(hilbert.asJson))

  test("I.3 Encoder for IdProf"):
    assertEquals(parse(idProfJson), Right(hilbertId.asJson))

  test("II.1 Decoder for Prof"):
    assertEquals(parse(profJson).flatMap(_.as[Prof]), Right(hilbert))

  test("II.2 Decoder for IdProf"):
    assertEquals(parse(idProfJson).flatMap(_.as[IdProf]), Right(hilbertId))

  test("III.1 IdProf => Prof"):
    profs.foreach((p, pId) => assertEquals(pId.toProf, p))

  test("III.2 Prof => IdProf"):
    given GenUUID[Id] with
      def randomUUID: UUID = UUID.randomUUID

    assertEquals(hilbert.genId[Id].toProf, hilbert)
