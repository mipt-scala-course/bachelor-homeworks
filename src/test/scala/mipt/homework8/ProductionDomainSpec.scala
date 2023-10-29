package mipt.homework8

import zio.*
import zio.test.*
import zio.test.Assertion.*


object ProductionDomainSpec extends ZIOSpecDefault:

    override def spec =
        suite("Factory test suite")(
            exampleTest
        )

    private def exampleTest =
        test("correctly create factories") {
            ZIO.succeed(assert( 1 != 2 )( isTrue ) )
        }
