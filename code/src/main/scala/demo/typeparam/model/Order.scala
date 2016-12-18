package demo.typeparam.model

import demo.common.{Behavior, BehaviorDsl, BehaviorBuilder}

case class Order(id: Long, customerNr: String, items: List[Item] = List.empty) {

  def orderActions =
    Order.actions
      .addCmdHandler {
        case AddItem(item) => List(ItemWasAdded(id, item))
        case RemoveItem(code) => List(ItemWasRemoved(id, code))
        case RemoveAllItems =>
          items.map { item =>
            ItemWasRemoved(id, item.code)
          }
      }
      .addEvtHandler {
        case e: ItemWasAdded => copy(items = e.item :: items)
        case e: ItemWasRemoved => copy(items = items.filter(_.code != e.code))
      }

}

object Order {

  def actions = BehaviorBuilder[Order, OrderCommand, OrderEvent]()

  val constructorActions =
    actions
      .addCmdHandler {
        case cmd: CreateOrder => List(OrderWasCreated(cmd.id, cmd.customerNr))
      }
      .addEvtHandler {
        case e: OrderWasCreated => Order(e.orderId, e.customerNr)
      }

  def behavior =
    BehaviorDsl
      .first(constructorActions)
      .andThen {
        case order => order.orderActions
      }

}

sealed trait OrderCommand

case class CreateOrder(id: Long, customerNr: String) extends OrderCommand

case class AddItem(item: Item) extends OrderCommand

case object RemoveAllItems extends OrderCommand

case class RemoveItem(code: String) extends OrderCommand

sealed trait OrderEvent

case class OrderWasCreated(orderId: Long, customerNr: String) extends OrderEvent

case class ItemWasAdded(orderId: Long, item: Item) extends OrderEvent

case class ItemWasRemoved(orderId: Long, code: String) extends OrderEvent

/** Example to illustrate how it would look like to implement a Behavior by hand */
object OrderBehavior extends Behavior[Order, OrderCommand, OrderEvent] {

  def onCmd(cmd: OrderCommand): List[OrderEvent] = {
    cmd match {
      case c: CreateOrder => List(OrderWasCreated(c.id, c.customerNr))
      case _ => List.empty
    }
  }

  def onCmd(agg: Order, cmd: OrderCommand): List[OrderEvent] = {
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

  def onEvt(evt: OrderEvent): Order = {
    evt match {
      case e: OrderWasCreated => Order(e.orderId, e.customerNr)
      case _ => ???
    }
  }

  def onEvt(order: Order, evt: OrderEvent): Order = {
    evt match {
      case e: ItemWasAdded => order.copy(items = e.item :: order.items)
      case e: ItemWasRemoved => order.copy(items = order.items.filter(_.code != e.code))
      case _ => order
    }
  }
}
