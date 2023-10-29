package mipt.homework8

enum ProductionItem(val value: String):
  case Sayany  extends ProductionItem("Sparkling Water Саяны (Франция)")
  case Kvas    extends ProductionItem("Квас 'Лягаевский'")
  case Compot  extends ProductionItem("Калинские компоты")
  case RosTish extends ProductionItem("Напитки для детей 'РосТишкин' (Россия)")

