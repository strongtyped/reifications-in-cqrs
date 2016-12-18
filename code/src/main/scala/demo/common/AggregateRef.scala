package demo.common

import scala.annotation.tailrec

class AggregateRef[Agg, Cmd, Evt](behavior: Behavior[Agg, Cmd, Evt], var aggOpt: Option[Agg] = None, var evts: List[Evt] = List.empty) {

  def getEvents: List[Evt] = evts.reverse

  def state(): Agg = aggOpt.get

  def !(cmd: Cmd): AggregateRef[Agg, Cmd, Evt] = {

    val newEvts = aggOpt match {
      case None => behavior.onCreateCmd(cmd)
      case Some(agg) => behavior.onCreatedCmd(agg)(cmd)
    }

    aggOpt = applyEvents(aggOpt, newEvts)

    evts = newEvts ::: evts

    this
  }

  @tailrec
  private def applyEvents(aggOpt: Option[Agg], evts: List[Evt]): Option[Agg] = {
    (aggOpt, evts) match {
      case (_, Nil) =>
        aggOpt
      case (None, head :: tail) =>
        applyEvents(Some(behavior.onCreateEvt(head)), tail)
      case (Some(aggregate), _) =>
        Some(evts.foldLeft(aggregate)((agg, cmd) => behavior.onCreatedEvt(agg)(cmd)))
    }
  }
}
