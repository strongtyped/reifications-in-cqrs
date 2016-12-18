package demo.pathdeptypes

import demo.pathdeptypes.model._

object MainPathDependentType {

  def app() = {

    println("--------------------------------------------------------")
    println("Path Dependent Types")
    println("")

    orderBackendDependentType.configure(Order.behavior)
    val orderRef = orderBackendDependentType.aggregateRef

    val orderCmds: List[OrderCmd] = List(
      CreateOrder(1, "abc"),
      AddItem(Item("001-FG", "product 1", 10)),
      AddItem(Item("002-HI", "product 2", 100)),
      AddItem(Item("003-LM", "product 3", 1000)),
      RemoveItem("003-LM")
    )

    orderCmds.foldLeft(orderRef)(_ ! _)

    print(orderCmds.mkString("Cmd: ", "\nCmd: ", ""))

    println()
    println()

    print(orderRef.getEvents.mkString("Evt: ", "\nEvt: ", ""))

    println()
    println()

    println(orderRef.state())

    println("--------------------------------------------------------")
  }
}
