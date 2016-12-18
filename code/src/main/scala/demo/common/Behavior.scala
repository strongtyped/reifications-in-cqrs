package demo.common

import demo.{CmdHandler, EvtHandler}

trait Behavior[Agg, Cmd, Evt] {

  val onCreateCmd: CmdHandler[Cmd, Evt]
  val onCreateEvt: EvtHandler[Evt, Agg]

  val onCreatedCmd: Agg => CmdHandler[Cmd, Evt]
  val onCreatedEvt: Agg => EvtHandler[Evt, Agg]

}