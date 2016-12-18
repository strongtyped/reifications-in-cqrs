package demo.pathdeptypes.model

import demo.common.Behavior
import demo.pathdeptypes.{Aggregate, BackendDependentType}
import demo.{CmdHandler, EvtHandler}

sealed trait OrderCmd

case class CreateOrder(id: Long, customerNr: String) extends OrderCmd

case class AddItem(item: Item) extends OrderCmd

case class RemoveItem(code: String) extends OrderCmd

case object RemoveAllItems extends OrderCmd


sealed trait OrderEvt

case class OrderCreated(orderId: Long, customerNr: String) extends OrderEvt

case class ItemAdded(orderId: Long, item: Item) extends OrderEvt

case class ItemRemoved(orderId: Long, code: String) extends OrderEvt


object Order extends Aggregate[Order] {

  override type Cmd = OrderCmd
  override type Evt = OrderEvt

  override val behavior =
    new Behavior[Order, Cmd, Evt] {
      override val onCreateCmd = {
        case CreateOrder(id, custNum) => List(OrderCreated(id, custNum))
      }
      override val onCreateEvt = {
        case OrderCreated(id, custNum) => Order(id, custNum)
      }
      override val onCreatedCmd = {
        order: Order => order.createdCmdHandler
      }
      override val onCreatedEvt = {
        order: Order => order.createdEvtHandler
      }

    }

}

import demo.pathdeptypes.model.Order._

case class Order(id: Long, customerNr: String, items: List[Item] = List.empty) {

  private val createdCmdHandler: CmdHandler[Cmd, Evt] = {
    case AddItem(item) => List(ItemAdded(id, item))
    case RemoveItem(code) => List(ItemRemoved(id, code))
    case RemoveAllItems => items.map { item => ItemRemoved(id, item.code) }
  }

  private val createdEvtHandler: EvtHandler[Evt, Order] = {
    case ItemAdded(_, item) => copy(items = item :: items)
    case ItemRemoved(_, code) => copy(items = items.filter(_.code != code))
  }

}

object orderBackendDependentType extends BackendDependentType[Order] {
  override implicit val aggregate: Order.type = Order
}

