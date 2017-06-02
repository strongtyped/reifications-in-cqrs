package demo.typeparam.model

import demo.common.{ Actions, Behavior, BehaviorDsl }

case class Order(id: Long, customerNr: String, items: List[Item] = List.empty) {

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
        case cmd: CreateOrder => List(OrderWasCreated(cmd.id, cmd.customerNr))
      }
      .eventHandler {
        case e: OrderWasCreated => Order(e.orderId, e.customerNr)
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
