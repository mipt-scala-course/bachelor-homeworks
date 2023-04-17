package mipt.homework9

import org.scalatest.flatspec.AnyFlatSpec
import Task3.eventLoop
import org.scalatest.matchers.should.Matchers

class Task3Spec extends AnyFlatSpec with Matchers {

  "eventLoop" should "execute functions according to invocation order" in {

    val function1 = eventLoop { a: Int =>
      Thread.sleep(500)
      a * 10
    }
    val function2 = eventLoop { a: Int =>
      Thread.sleep(100)
      a * 5
    }

    var function1Executed = false
    var function2Executed = false

    function1(10, { a =>
      a shouldBe 100
      function1Executed shouldBe false
      function2Executed shouldBe false
      function1Executed = true
    })

    function1Executed shouldBe false
    function2Executed shouldBe false

    function1(20, { a =>
      a shouldBe 200
      function1Executed shouldBe true
      function2Executed shouldBe false
      function1Executed = true
    })

    function2(5, { a =>
      a shouldBe 25
      function1Executed shouldBe true
      function2Executed = true
    })

    function2Executed shouldBe false

    Thread.sleep(1150)

    function1Executed shouldBe true
    function2Executed shouldBe true

  }

}
