package demo.param

import demo.param.model.{ AddItem, _ }

object MainTypeParameters extends App {

  println("--------------------------------------------------------")
  println("Type Parameters")
  println("")

  val ref = new AggregateRef(OrderBehavior)

  ref ? CreateOrder(1, "abc")
  ref ? AddItem(Item("001-FG", "product 1", 10))
  ref ? AddItem(Item("002-HI", "product 2", 100))
  ref ? AddItem(Item("003-LM", "product 3", 1000))
  ref ? RemoveItem("003-LM")

  ref.getEvents.foreach { evt =>
    println(s"Event: $evt")
  }
  println(ref.state())
  println("--------------------------------------------------------")
}
