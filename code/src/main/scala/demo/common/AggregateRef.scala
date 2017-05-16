package demo.common

class AggregateRef[A, Cmd, Evt](behavior: Behavior[A, Cmd, Evt]) {

  private var aggOpt: Option[A] = None

  private var allEvents: List[Evt] = List.empty

  def getEvents = allEvents.reverse

  def state() = aggOpt.get

  def !(cmd: Cmd): List[Evt] = {

    // initialize or update aggregate

    val evts = behavior.onCommand(aggOpt, cmd)

    // apply all events to aggregate
    aggOpt = applyEvents(aggOpt, evts)

    // update aggregate
    allEvents = evts ::: allEvents

    evts
  }

  private def applyEvents(aggOpt: Option[A], evts: List[Evt]): Option[A] = {
    evts.foldLeft(aggOpt) { (agg, evt) =>
      behavior.onEvent(agg, evt)
    }
  }
}
