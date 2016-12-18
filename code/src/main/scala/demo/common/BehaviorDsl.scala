package demo.common

object BehaviorDsl {

  def first[Agg, Cmd, Evt](createHandlers: => BehaviorBuilder[Agg, Cmd, Evt]) =
    new BehaviorStage(createHandlers)

  class BehaviorStage[Agg, Cmd, Evt](createHandlers: => BehaviorBuilder[Agg, Cmd, Evt]) {

    def andThen(createdHandlers: Agg => BehaviorBuilder[Agg, Cmd, Evt]): Behavior[Agg, Cmd, Evt] = {
      new Behavior[Agg, Cmd, Evt] {
        def onCmd(cmd: Cmd): List[Evt] = createHandlers.cmdHandlers(cmd)

        def onEvt(evt: Evt): Agg = createHandlers.evtHandlers(evt)

        def onCmd(agg: Agg, cmd: Cmd): List[Evt] = createdHandlers(agg).cmdHandlers(cmd)

        def onEvt(agg: Agg, evt: Evt): Agg = createdHandlers(agg).evtHandlers(evt)
      }
    }

  }

}
