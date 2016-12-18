package demo.common

trait Behavior[Agg, Cmd, Evt] {

  def onCmd(cmd: Cmd): List[Evt]

  def onCmd(agg: Agg, cmd: Cmd): List[Evt]

  def onEvt(evt: Evt): Agg

  def onEvt(agg: Agg, evt: Evt): Agg
}
