package examples

object Example2_OpticShop:
  /*    ===========   ORDERS    ============
        | CLIENTS |  ========>  | OUR SHOP |===============|
        ===========             ============              ||
                /\    DELIVERY   ||      /\               ||
                |=================|      || SUPPLY        || ORDERS
                                         ||               \/
                                         ||  =================
                                         |===| GLASS FACTORY |
                                             =================
   */

  case class User(id: Int, name: String, address: String)
  enum Item:
    case Glasses
    case Monocle
    case MagnifyingGlass
    case Packaging(item: Item, count: Int)
  case class Order(items: Vector[Item], user: User)

  trait Client:
    def makeOrder(items: Vector[Item]): Unit

  trait Shop:
    def addNewClient(client: Client): Unit
    
  trait Factory:
    def subscribeShop(shop: Shop): Unit
    