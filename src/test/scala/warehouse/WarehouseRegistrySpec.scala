package warehouse

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

class WarehouseRegistrySpec extends AnyFlatSpec, Matchers:
  val warehouseWithPrefix: WarehouseRegistry = WarehouseRegistry(
    Vector(
      Item.SingleItem("OldPrefix@glasses", 15),
      Item.Packaging("OldPrefix@magnifying glass", 1, 100)
    ),
    Vector(
      Item.SingleItem("OldPrefix@monocle", 10)
    )
  )
  val warehouseWithoutPrefix: WarehouseRegistry = WarehouseRegistry(
    Vector(
      Item.SingleItem("glasses", 15),
      Item.Packaging("magnifying glass", 1, 100)
    ),
    Vector(
      Item.SingleItem("monocle", 10)
    )
  )

  it should "change prefix" in {
    WarehouseRegistry.warehouseNameOptic.update(warehouseWithPrefix, _ => "NewPrefix") shouldBe
      WarehouseRegistry(
        Vector(
          Item.SingleItem("NewPrefix@glasses", 15),
          Item.Packaging("NewPrefix@magnifying glass", 1, 100)
        ),
        Vector(
          Item.SingleItem("NewPrefix@monocle", 10)
        )
      )
  }

  it should "not add prefix" in {
    WarehouseRegistry.warehouseNameOptic.update(warehouseWithoutPrefix, _ => "NewPrefix") shouldBe
      warehouseWithoutPrefix
  }
