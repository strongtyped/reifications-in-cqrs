package demo.common

import scala.annotation.tailrec

class AggregateRef[Agg, Cmd, Evt](behavior: Behavior[Agg, Cmd, Evt]) {

  private var aggOpt: Option[Agg] = None

  private var allEvents: List[Evt] = List.empty

  def getEvents = allEvents.reverse

  def state() = aggOpt.get

  def !(cmd: Cmd): AggregateRef[Agg, Cmd, Evt] = {

    // initialize or update aggregate

    val evts = aggOpt match {
      case None => behavior.onCmd(cmd)
      case Some(agg) => behavior.onCmd(agg, cmd)
    }

    // apply all events to aggregate
    aggOpt = applyEvents(aggOpt, evts)

    // update events
    allEvents = evts ::: allEvents

    this
  }

  @tailrec
  private def applyEvents(aggOpt: Option[Agg], evts: List[Evt]): Option[Agg] = {
    (aggOpt, evts) match {
      case (_, Nil) =>
        aggOpt
      case (None, head :: tail) =>
        applyEvents(Some(behavior.onEvt(head)), tail)
      case (Some(aggregate), _) =>
        Some(evts.foldLeft(aggregate)(behavior.onEvt))
    }
  }
}