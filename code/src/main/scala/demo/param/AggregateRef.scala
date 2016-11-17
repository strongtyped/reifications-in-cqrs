package demo.param

trait Behavior[A, C, E] {

  def onCommand(cmd: C): List[E]

  def onCommand(agg: A, cmd: C): List[E]

  def onEvent(evt: E): A

  def onEvent(agg: A, evt: E): A
}

class AggregateRef[A, C, E](behavior: Behavior[A, C, E]) {

  private var aggOpt: Option[A] = None

  private var allEvents: List[E] = List.empty

  def getEvents = allEvents.reverse
  def state()   = aggOpt.get

  def ?(cmd: C): List[E] = {

    // initialize or update aggregate

    val evts = aggOpt match {
      case None      => behavior.onCommand(cmd)
      case Some(agg) => behavior.onCommand(agg, cmd)
    }

    // apply all events to aggregate
    aggOpt = applyEvents(aggOpt, evts)

    // update aggregate
    allEvents = evts ::: allEvents

    evts
  }

  private def applyEvents(aggOpt: Option[A], evts: List[E]): Option[A] = {
    (aggOpt, evts) match {
      case (Some(aggregate), _) =>
        val updated =
          evts.foldLeft(aggregate) { (agg, evt) =>
            behavior.onEvent(agg, evt)
          }
        Some(updated)

      case (None, head :: tail) =>
        val agg = Some(behavior.onEvent(head))
        applyEvents(agg, tail)
    }
  }
}
