//package demo.common
//
//object BehaviorDsl {
//
//  def first[Agg, Cmd, Evt](create: => BehaviorBuilder[Agg, Cmd, Evt]) =
//    new BehaviorStage(create)
//
//  class BehaviorStage[Agg, Cmd, Evt](create: => BehaviorBuilder[Agg, Cmd, Evt]) {
//
//    def andThen(created: Agg => BehaviorBuilder[Agg, Cmd, Evt]): Behavior[Agg, Cmd, Evt] =
//      new Behavior[Agg, Cmd, Evt] {
//        def onCmd(cmd: Cmd): List[Evt] = create.cmdHandlers.apply(cmd)
//
//        def onEvt(evt: Evt): Agg = create.evtHandlers.apply(evt)
//
//        def onCmd(agg: Agg, cmd: Cmd): List[Evt] = created(agg).cmdHandlers.apply(cmd)
//
//        def onEvt(agg: Agg, evt: Evt): Agg = created(agg).evtHandlers.apply(evt)
//      }
//
//  }
//
//}
