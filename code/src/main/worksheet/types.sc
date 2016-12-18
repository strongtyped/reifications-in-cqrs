import ActionsDsl.ActionsFactory

trait Types {
  type Id
  type Aggregate
  type Command
  type Event

  def behavior(id: Id): Behavior[this.type]
}

case class Order(id: Long, customerNr: String, items: List[String] = List.empty)

sealed trait OrderCommand

case class CreateOrder(id: Long, customerNr: String) extends OrderCommand

case class AddItem(item: String) extends OrderCommand

case object RemoveAllItems extends OrderCommand

case class RemoveItem(code: String) extends OrderCommand


sealed trait OrderEvent

case class OrderWasCreated(orderId: Long, customerNr: String) extends OrderEvent

case class ItemWasAdded(orderId: Long, item: String) extends OrderEvent

case class ItemWasRemoved(orderId: Long, code: String) extends OrderEvent

object Order extends Types {

  type Id = Long
  type Aggregate = Order
  type Command = OrderCommand
  type Event = OrderEvent

  val actions: ActionsFactory[Order.this.type] = _

  def behavior(id: Long): Behavior[Order.this.type] = ???

}

case class Behavior[T <: Types](types: T) {
  def onCommand(cmd: types.Command): types.Event = ???

  def onEvent(evt: types.Event): types.Aggregate = ???
}

object ActionsDsl {

  class ActionsFactory[T <: Types](types: T) {
    def handleCommand[C <: types.Command, E <: types.Event](cmd: C => E) = Actions(types, List("C"))

    def handleEvent[E <: types.Event, A <: types.Aggregate](cmd: E => A) = Actions(types, List("E"))
  }

  case class Actions[T <: Types](types: T, act: List[String] = List.empty) {
    def handleCommand[C <: types.Command, E <: types.Event](cmd: C => E) = copy(act = act :+ "C")

    def handleEvent[E <: types.Event, A <: types.Aggregate](cmd: E => A) = copy(act = act :+ "E")
  }

  def actions[T <: Types](types: T) = new ActionsFactory(types)

}

val fact = ActionsDsl.actions(Order)

fact.handleCommand {
  c: CreateOrder => OrderWasCreated(c.id, c.customerNr)
}.handleEvent {
  e: OrderWasCreated => Order(e.orderId, e.customerNr)
}.handleEvent {
  e: OrderWasCreated => Order(e.orderId, e.customerNr)
}.handleCommand {
  c: CreateOrder => OrderWasCreated(c.id, c.customerNr)
}


