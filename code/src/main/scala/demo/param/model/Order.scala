package demo.param.model

import demo.param.Behavior

case class Order(id: Long, customerNr: String, items: List[Item] = List.empty)

sealed trait OrderCommand
case class CreateOrder(id: Long, customerNr: String) extends OrderCommand
case class AddItem(item: Item) extends OrderCommand
case object RemoveAllItems extends OrderCommand
case class RemoveItem(code: String) extends OrderCommand

sealed trait OrderEvent
case class OrderWasCreated(orderId: Long, customerNr: String) extends OrderEvent
case class ItemWasAdded(orderId: Long, item: Item) extends OrderEvent
case class ItemWasRemoved(orderId: Long, code: String) extends OrderEvent

object OrderBehavior extends Behavior[Order, OrderCommand, OrderEvent] {

  def onCommand(cmd: OrderCommand): List[OrderEvent] = {
    cmd match {
      case c: CreateOrder => List(OrderWasCreated(c.id, c.customerNr))
      case _              => List.empty
    }
  }

  def onCommand(agg: Order, cmd: OrderCommand): List[OrderEvent] = {
    cmd match {

      case AddItem(item) => List(ItemWasAdded(agg.id, item))

      case RemoveItem(code) => List(ItemWasRemoved(agg.id, code))

      case RemoveAllItems =>
        agg.items.map { item =>
          ItemWasRemoved(agg.id, item.code)
        }

      case _ => List.empty
    }

  }

  def onEvent(evt: OrderEvent): Order = {
    evt match {
      case e: OrderWasCreated => Order(e.orderId, e.customerNr)
      case _                  => ???
    }
  }

  def onEvent(order: Order, evt: OrderEvent): Order = {
    evt match {
      case e: ItemWasAdded   => order.copy(items = e.item :: order.items)
      case e: ItemWasRemoved => order.copy(items = order.items.filter(_.code != e.code))
      case _                 => ???
    }
  }
}
