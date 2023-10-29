package mipt.lecture7and8

import scala.util.boundary
import scala.util.boundary.*
import scala.annotation.tailrec


object sheetB_Boundary extends App:


    def firstIndexFolded[T](xs: List[T], elem: T): Int =
        xs.zipWithIndex
            .foldLeft(Option.empty[Int]){ case (acc, (next, index)) =>
                if (acc.isEmpty && next == elem) Some(index) else None
            }.getOrElse(-1)






    def firstIndexRecurrent[T](xs: List[T], elem: T): Int =
        @tailrec
        def run[T](_xs: List[T], index: Int): Int =
            _xs match
                case head :: tail if head == elem => index
                case head :: tail => run(tail, index + 1)
                case Nil => -1
        run(xs, 0)
        



















    def firstIndex[T](xs: List[T], elem: T): Int =
        boundary:
            for (x, i) <- xs.zipWithIndex do
                if (x == elem) then break(i)
            -1






    val list = "a" :: "b" :: "c" :: "d" :: Nil

    println(firstIndex(list, "x"))
//  > 2






    // Что можно с этим делать. Примеры.


    import optional.*
    def firstColumn[T](xss: List[List[T]]): Option[List[T]] =
        optional:
            xss.map(_.headOption.?)



    val table = List(
        "col1"  :: "col2"  :: "col3"  :: Nil,
        "row01" :: Nil,
        "row11" :: "row21" :: "row23" :: Nil,
        "row21" :: "row22" :: "row23" :: Nil
    )

    println(firstColumn(table))




    //
    

    object optional:
        inline def apply[T](inline body: Label[None.type] ?=> T): Option[T] =
            boundary(Some(body))

        extension [T] (r: Option[T])
            inline def ? (using label: Label[None.type]): T =
                r match
                    case Some(value) => value
                    case None => break(None)
    end optional

    



