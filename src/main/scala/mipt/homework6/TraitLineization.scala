package mipt.homework6

import mipt.utils.Homeworks._

object TraitLineization {

  type MethodDef = String
  case class TraitDef(parents: List[TraitDef], methods: Map[String, MethodDef])

  def resolveMethod(traitDef: TraitDef, method: String): Option[MethodDef] =
    task"""
          Реализуйте алгоритм линеизации цепочки наследования типов и определения метода для вызова из корректного типа
        """ (1, 1)

}
