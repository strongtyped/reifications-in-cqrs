package demo.pathdeptypes.model

import demo.common.BehaviorDsl
import demo.pathdeptypes.Types

case class Order(id: Long, customerNr: String, items: List[Item] = List.empty) {

  def orderActions =
    Order.actions
      .handleCommand {
        case AddItem(item)    => List(ItemWasAdded(id, item))
        case RemoveItem(code) => List(ItemWasRemoved(id, code))
        case RemoveAllItems =>
          items.map { item =>
            ItemWasRemoved(id, item.code)
          }
      }
      .handleEvent {
        case e: ItemWasAdded   => copy(items = e.item :: items)
        case e: ItemWasRemoved => copy(items = items.filter(_.code != e.code))
      }

}

object Order extends Types[Order] {

  type Command = OrderCommand
  type Event   = OrderEvent

  val constructorActions =
    actions
      .handleCommand {
        case CreateOrder(id, custNum) => List(OrderWasCreated(id, custNum))
      }
      .handleEvent {
        case OrderWasCreated(id, custNum) => Order(id, custNum)
      }

  def behavior =
    BehaviorDsl
      .construct(constructorActions)
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
