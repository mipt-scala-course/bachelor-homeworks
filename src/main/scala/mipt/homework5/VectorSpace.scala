package mipt.homework5

/**
Реализовать тайпкласс VectorSpace[V, F]
  Векторное пространство - это поле F и множество V, на котором

    - определена операция сложения (+: (V, V) => V)
    - есть нейтральный элемент относительно сложения (0: V)
    - определена операция умножения на элементы F (*: (V, F) => V)

  И выполнены законы:
    1) (V, +, 0) - абелева группа
    2) forall v: V => v + (-v) = 0
    3) forall a, b: F, v: V => a(bv) = (ab)v
    4) forall v: V => v * 1 = v
    5) forall a: F, u, v: V => a(u + v) = au + av
    6) forall a, b: F, v: V => (a + b)v = av + bv
 */
trait VectorSpace[V, F] extends Field[F] {
  // названия методов могут выбраны произвольно
  //  def empty
  //  def addV
  //  def mulS
}

object VectorSpace {
  /**
  Реализовать summoner для VectorSpace
    После реализации вместо `implicitly[VectorSpace[Voo, Foo]]` можно будет писать просто `VectorSpace[Voo, Foo]`
   */
  //  def apply...

  /**
  Реализовать инстанс векторного пространство из поля над самим собой
   */
  implicit def fieldInstance[F](implicit field: Field[F]): VectorSpace[F, F] = ???

  /**
  Реализовать инстанс векторного пространство из ф-ций X => F над полем F
   */
  implicit def funcInstance[X, F](implicit F: Field[F]): VectorSpace[X => F, F] = ???

  /**
  Есть векторное пространство V над полем F
    Построить вектроное пространство (V, V) (декартово произвдение) над полем F
   */
  implicit def pairInstance[V, F](implicit space: VectorSpace[V, F]): VectorSpace[(V, V), F] = ???

  /**
  Реализовать синтаксис для операций из тайпкласса VectorSpace
   */
  object syntax {
    //    implicit class VectorSpaceSyntax...
  }

  /**
  Релизовать метод вычисляющий линейную комбинацию векторов
      v = (v1 * f1) + (v2 * f2) + ... + (vN * fN)
   */
  def linearCombination[V, F](values: Vector[(V, F)])(implicit space: VectorSpace[V, F]): V = ???
}

