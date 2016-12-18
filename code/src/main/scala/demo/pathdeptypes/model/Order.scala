package demo.pathdeptypes.model

import demo.pathdeptypes.Aggregate
import demo.{CmdHandler, EvtHandler}


object Order extends Aggregate[Order] {

  override type Cmd = OrderCmd
  override type Evt = OrderEvt

  override val createCmdHandler: CmdHandler[Cmd, Evt] = {
    case CreateOrder(id, custNum) => List(OrderCreated(id, custNum))
  }

  override val createEvtHandler: EvtHandler[Evt, Order] = {
    case OrderCreated(id, custNum) => Order(id, custNum)
  }

  override val createdCmdHandler: Order => CmdHandler[Cmd, Evt] =
    (order: Order) =>
      order.createdCmdHandler

  override val createdEvtHandler: Order => EvtHandler[Evt, Order] =
    (order: Order) =>
      order.createdEvtHandler

}

case class Order(id: Long, customerNr: String, items: List[Item] = List.empty) {

  private val createdCmdHandler: CmdHandler[Order.Cmd, Order.Evt] = {
    case AddItem(item) => List(ItemAdded(id, item))
    case RemoveItem(code) => List(ItemRemoved(id, code))
    case RemoveAllItems =>
      items.map { item =>
        ItemRemoved(id, item.code)
      }
  }

  private val createdEvtHandler: EvtHandler[Order.Evt, Order] = {
    case e: ItemAdded => copy(items = e.item :: items)
    case e: ItemRemoved => copy(items = items.filter(_.code != e.code))
  }

}

sealed trait OrderCmd

case class CreateOrder(id: Long, customerNr: String) extends OrderCmd

case class AddItem(item: Item) extends OrderCmd

case class RemoveItem(code: String) extends OrderCmd

case object RemoveAllItems extends OrderCmd


sealed trait OrderEvt

case class OrderCreated(orderId: Long, customerNr: String) extends OrderEvt

case class ItemAdded(orderId: Long, item: Item) extends OrderEvt

case class ItemRemoved(orderId: Long, code: String) extends OrderEvt
