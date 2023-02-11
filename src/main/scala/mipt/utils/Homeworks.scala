package mipt.utils

object Homeworks {

  final case class TaskNotDone(num: String, text: String) extends RuntimeException(s"выполните задание $num : \n $text")

  trait TaskDef {
    def applySeq(num: Seq[Int]): Nothing

    def apply(num: Int*): Nothing = applySeq(num)
  }

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  implicit class TaskSyntax(private val cs: StringContext) extends AnyVal {
    def task(refs: Any*): TaskDef = xs => {
      val message = cs.s(refs: _*).stripMargin
      throw TaskNotDone(xs.mkString("."), message)
    }
  }

}
