package demo.common

import demo._

case class BehaviorBuilder[Agg, Cmd, Evt]
(cmdHandlers: CmdHandler[Cmd, Evt] = PartialFunction.empty,
 evtHandlers: EvtHandler[Evt, Agg] = PartialFunction.empty) {

  def addCmdHandler(cmdHandler: CmdHandler[Cmd, Evt]): BehaviorBuilder[Agg, Cmd, Evt] =
    copy(cmdHandlers = cmdHandlers orElse cmdHandler)

  def addEvtHandler(evtHandler: EvtHandler[Evt, Agg]): BehaviorBuilder[Agg, Cmd, Evt] =
    copy(evtHandlers = evtHandlers orElse evtHandler)
}
