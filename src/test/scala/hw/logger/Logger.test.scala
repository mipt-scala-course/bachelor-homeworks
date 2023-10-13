package hw.logger

import cats.data.WriterT
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.util.{Success, Try}

class Tests extends AnyFlatSpec with Matchers:
  def logEmbed(debug: Vector[String], info: Vector[String], error: Vector[String]): LogEmbed[Try, Unit] =
    LogEmbed(WriterT(WriterT(WriterT(Success((debug.map(Debug.apply), (info.map(Info.apply), (error.map(Error.apply), ()))))))))

  it should "info log" in {
    val logger = Logger[Try]()
    logger.info("info") shouldBe logEmbed(Vector.empty, Vector("info"), Vector.empty)
  }

  it should "debug log" in {
    val logger = Logger[Try]()
    logger.debug("debug") shouldBe logEmbed(Vector("debug"), Vector.empty, Vector.empty)
  }

  it should "error log" in {
    val logger = Logger[Try]()
    logger.error("error") shouldBe logEmbed(Vector.empty, Vector.empty, Vector("error"))
  }

  it should "log everything" in {
    val logger = Logger[Try]()
    (for {
      _ <- logger.info("info").value
      _ <- logger.debug("debug").value
      _ <- logger.error("error").value
    } yield ()) shouldBe logEmbed(Vector("debug"), Vector("info"), Vector("error")).value
  }