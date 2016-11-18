package demo.common

class AggregateRef[A, Cmd, Evt](behavior: Behavior[A, Cmd, Evt]) {

  private var aggOpt: Option[A] = None

  private var allEvents: List[Evt] = List.empty

  def getEvents = allEvents.reverse
  def state()   = aggOpt.get

  def !(cmd: Cmd): List[Evt] = {

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

  private def applyEvents(aggOpt: Option[A], evts: List[Evt]): Option[A] = {
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

      case (None, Nil) => aggOpt
    }
  }
}
