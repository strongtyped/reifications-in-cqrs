package object demo {

  type CmdHandler[Cmd, Evt] = PartialFunction[Cmd, List[Evt]]
  type EvtHandler[Evt, Agg] = PartialFunction[Evt, Agg]

}
