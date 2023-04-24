package mipt.homework10

object Auxiliary {

    trait Semigroup[A] {
        def combine(left: A, right: A): A
    }

    object Semigroup {

        implicit val unitSemigroup: Semigroup[Unit] =
            new Semigroup[Unit] {
                override def combine(left: Unit, right: Unit): Unit = ()
            }

        def apply[A](implicit inst: Semigroup[Unit]): Semigroup[Unit] = inst
    }

    // Odeum
    case class Odeum[A](see: String => Unit, listen: String => Unit)

    object Odeum {
        def apply[A](implicit odeum: Odeum[A]): Odeum[A] =
            odeum
    }
    
}
