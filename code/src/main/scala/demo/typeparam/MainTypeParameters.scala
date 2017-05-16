package demo.typeparam

import demo.Show
import demo.typeparam.model._

object MainTypeParameters {

  def main(args: Array[String]): Unit = {

    BackendParam.configure(Order.behavior)

    val orderRef = BackendParam.aggregateRef[Order, OrderCommand, OrderEvent]

    orderRef ! CreateOrder(1, "abc")
    orderRef ! AddItem(Item("001-FG", "product 1", 10))
    orderRef ! AddItem(Item("002-HI", "product 2", 100))
    orderRef ! AddItem(Item("003-LM", "product 3", 1000))
    orderRef ! RemoveItem("003-LM")

    Show("Type Parameters")(orderRef.state(), orderRef.getEvents)

  }
}
