package context

import cats.data.Reader
import cats.syntax.applicative.*
import cats.syntax.flatMap.*
import cats.{Applicative, FlatMap}
import context.ContextOpsSpec.{ConfigReader, context}
import glass.Contains
import glass.macros.GenContains
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

object ContextOpsSpec:
  trait ConfigReader[F[_]]:
    def read: F[Int]

  class ConfigReaderConst[F[_]: Applicative] extends ConfigReader[F]:
    override def read: F[Int] = 42.pure[F]

  given Embed[ConfigReader] = new Embed[ConfigReader]:
    override def embed[F[_]: FlatMap](a: F[ConfigReader[F]]): ConfigReader[F] = new ConfigReader[F]:
      override def read: F[Int] = a.flatMap(_.read)

  case class Context(a: Int, b: ConfigReader[F])

  given Contains[Context, Int]             = GenContains[Context](_.a)
  given Contains[Context, ConfigReader[F]] = GenContains[Context](_.b)

  type F[x] = Reader[Context, x]

  val context = Context(2023, new ConfigReaderConst[F])

class ContextOpsSpec extends AnyFlatSpec, Matchers, ContextOps[ContextOpsSpec.Context, ContextOpsSpec.F]:
  import ContextOpsSpec.*

  it should "return value from context" in {
    getSubContext[Int](context) shouldBe context.a
  }

  it should "return embed from context" in {
    getEmbedSubContext[ConfigReader].read(context) shouldBe context.b.read(context)
  }
