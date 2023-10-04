package mipt.traverse

import java.time.ZonedDateTime
import cats.Applicative
import mipt.traverse.FormLocalization.localize
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class FormLocalizationTest extends AnyFlatSpec with Matchers:
  behavior.of("FormLocalization")

  given Applicative[Option] = cats.instances.option.catsStdInstancesForOption
  given Localization[Option] = new Localization[Option]:
    val keys = Map(
      Template("origination")  -> Rendered("Ориджинейшн"),
      Template("applications") -> Rendered("Заявки"),
      Template("products")     -> Rendered("Продукты"),
      Template("applicant")    -> Rendered("Заявитель"),
      Template("eio")          -> Rendered("Единоличный исполнительный орган")
    )
    def localize: Template => Option[Rendered] = keys.get

  val now = ZonedDateTime.now()
  val products = Form(
    title = Template("products"),
    tags = List(Template("products")),
    subForms = List.empty,
    date = now
  )
  val applicant = Form(
    title = Template("applicant"),
    tags = List(Template("applicant")),
    subForms = List.empty,
    date = now
  )
  val origination = Form(
    title = Template("origination"),
    tags = List(Template("products"), Template("applicant"), Template("eio")),
    subForms = List(products, applicant),
    date = now
  )

  it should "localize form" in {
    localize(origination) shouldBe Some(
      Form(
        title = Rendered("Ориджинейшн"),
        tags = List(Rendered("Продукты"), Rendered("Заявитель"), Rendered("Единоличный исполнительный орган")),
        subForms = List(
          Form(
            title = Rendered("Продукты"),
            tags = List(Rendered("Продукты")),
            subForms = List.empty,
            date = now
          ),
          Form(
            title = Rendered("Заявитель"),
            tags = List(Rendered("Заявитель")),
            subForms = List.empty,
            date = now
          )
        ),
        date = now
      )
    )
  }
