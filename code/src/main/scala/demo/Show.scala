package demo

object Show {
  def apply[A, E](label: String)(agg: A, evts: Seq[E]) = {

    println()
    println(label)
    println("--------------------------------------------------------")
    println()

    evts.foreach { evt =>
      println(s"Event: $evt")
    }

    println()
    println(agg)
    println("________________________________________________________")
    println()
    println()

  }
}
