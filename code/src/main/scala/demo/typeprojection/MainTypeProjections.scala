package demo.typeprojection

import demo.Show
import demo.typeprojection.model._

object MainTypeProjections {

  def main(args: Array[String]): Unit = {

    BackendTypeProj.configure(Order.behavior)

    val orderRef = BackendTypeProj.aggregateRef[Order]

    orderRef ! CreateOrder(1, "abc")
    orderRef ! AddItem(Item("001-FG", "product 1", 10))
    orderRef ! AddItem(Item("002-HI", "product 2", 100))
    orderRef ! AddItem(Item("003-LM", "product 3", 1000))
    orderRef ! RemoveItem("003-LM")

    Show("Type Projections")(orderRef.state(), orderRef.getEvents)
  }
}
