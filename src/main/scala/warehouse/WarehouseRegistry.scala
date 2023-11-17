package warehouse

import glass.Items

enum Item:
  case SingleItem(name: String, price: Int)
  case Packaging(name: String, pricePerItem: Int, numberOfItems: Int)
case class WarehouseRegistry(shipped: Vector[Item], contained: Vector[Item])

/**
 * По данной модели товаров на складе опишите оптику для работы с префиксом названий этих товаров
 */
object WarehouseRegistry:
  /**
   * Создайте оптику, которая по warehouse.WarehouseRegistry будет обращаться к name всех warehouse.Item всех типов в
   * shipped и contained одновременно. Внутри name нас интересует, содержит ли он префикс, в данном случае
   * подстроку, заканчивающуюся на @. Если префикс есть, то мы фокусируемся на нём, а если нет - ни на чём.
   * Таким образом должна получиться оптика, меняющая имена warehouse.Item так, что, например, запрос .set("NEW")
   * к предмету SingleItem("OLD@itemName", 10) сделает его SingleItem("NEW@itemName", 10)
   */
  val warehouseNameOptic: Items[WarehouseRegistry, String] = ???
