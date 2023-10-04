package mipt.traverse

import java.time.ZonedDateTime
import cats.syntax.all.*
import cats.{Applicative, Eval, Traverse}

object Template:
  opaque type S = String

  def apply(s: String): S = s

type Template = Template.S

object Rendered:
  opaque type S = String

  def apply(s: String): S = s

type Rendered = Rendered.S

trait Localization[F[_]]:
  def localize: Template => F[Rendered]

case class Form[A](
    title: A,
    tags: List[A],
    subForms: List[Form[A]],
    date: ZonedDateTime
)

object FormLocalization:

  /** Реализуйте функцию, которая подставляет в форму вместо заглушек значения с помощью тайпкласса Localization
    * Локализованное значение вычисляется с эффектом, поэтому функция должна вычислять новый эффект с локализованной
    * формой
    */
  def localize[F[_]: Applicative: Localization](f: Form[Template]): F[Form[Rendered]] = ???
