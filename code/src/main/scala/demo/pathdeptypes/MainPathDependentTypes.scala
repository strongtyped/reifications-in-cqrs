package demo.pathdeptypes

import demo.pathdeptypes.model._

object MainPathDependentTypes {

  def main(args: Array[String]): Unit = {

    println("--------------------------------------------------------")
    println("Path Dependent Types")
    println("")

    BackendDependentType.configure(Order.behavior)
    val orderRef = BackendDependentType.aggregateRef[Order]

    orderRef ! CreateOrder(1, "abc")
    orderRef ! AddItem(Item("001-FG", "product 1", 10))
    orderRef ! AddItem(Item("002-HI", "product 2", 100))
    orderRef ! AddItem(Item("003-LM", "product 3", 1000))
    orderRef ! RemoveItem("003-LM")

    orderRef.getEvents.foreach { evt =>
      println(s"Event: $evt")
    }

    println(orderRef.state())
    println("--------------------------------------------------------")
  }
}
