package demo.typeprojection.model

import demo.common.{ Actions, BehaviorDsl }
import demo.typeprojection.Aggregate

case class Order(id: Long, customerNr: String, items: List[Item] = List.empty) extends Aggregate {

  type Command = OrderCommand
  type Event   = OrderEvent

  def orderActions =
    Order.actions
      .commandHandler {
        case AddItem(item)    => List(ItemWasAdded(id, item))
        case RemoveItem(code) => List(ItemWasRemoved(id, code))
        case RemoveAllItems =>
          items.map { item =>
            ItemWasRemoved(id, item.code)
          }
      }
      .eventHandler {
        case e: ItemWasAdded   => copy(items = e.item :: items)
        case e: ItemWasRemoved => copy(items = items.filter(_.code != e.code))
      }

}

object Order {

  val actions = Actions[Order, OrderCommand, OrderEvent]()

  val create =
    actions
      .commandHandler {
        case CreateOrder(id, custNum) => List(OrderWasCreated(id, custNum))
      }
      .eventHandler {
        case OrderWasCreated(id, custNum) => Order(id, custNum)
      }

  def behavior =
    BehaviorDsl
      .first {
        create
      }
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
