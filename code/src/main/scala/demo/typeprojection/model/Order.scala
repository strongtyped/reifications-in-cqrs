package demo.typeprojection.model

import demo.common.{BehaviorDsl, BehaviorBuilder}
import demo.typeprojection.{Aggregate, ProtocolLike}

case class Order(id: Long, customerNr: String, items: List[Item] = List.empty) extends Aggregate {

  type Protocol = OrderProtocol.type

  def orderActions =
    Order.actions
      .cmd {
        case AddItem(item) => List(ItemWasAdded(id, item))
        case RemoveItem(code) => List(ItemWasRemoved(id, code))
        case RemoveAllItems =>
          items.map { item =>
            ItemWasRemoved(id, item.code)
          }
      }
      .evt {
        case e: ItemWasAdded => copy(items = e.item :: items)
        case e: ItemWasRemoved => copy(items = items.filter(_.code != e.code))
      }

}

object OrderProtocol extends ProtocolLike {

  type Command = OrderCommand
  type Event = OrderEvent
}

object Order {

  val actions = BehaviorBuilder[Order, OrderProtocol.Command, OrderProtocol.Event]()

  val constructorActions =
    actions
      .cmd {
        case CreateOrder(id, custNum) => List(OrderWasCreated(id, custNum))
      }
      .evt {
        case OrderWasCreated(id, custNum) => Order(id, custNum)
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
