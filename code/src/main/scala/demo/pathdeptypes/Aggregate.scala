package demo.pathdeptypes

import demo.common.BehaviorDsl.first
import demo.common.{Behavior, BehaviorBuilder}
import demo.{CmdHandler, EvtHandler}

trait Types[Agg] {
  type Cmd
  type Evt
}

trait Aggregate[Agg] extends Types[Agg] {

  val createCmdHandler: CmdHandler[Cmd, Evt]

  val createEvtHandler: EvtHandler[Evt, Agg]

  val createdCmdHandler: Agg => CmdHandler[Cmd, Evt]

  val createdEvtHandler: Agg => EvtHandler[Evt, Agg]

  val behaviorBuilder = BehaviorBuilder[Agg, Cmd, Evt]()

  val behavior: Behavior[Agg, Cmd, Evt] =
    first {
      behaviorBuilder
        .addCmdHandler(createCmdHandler)
        .addEvtHandler(createEvtHandler)
    }.andThen {
      agg =>
        behaviorBuilder
          .addCmdHandler(createdCmdHandler(agg))
          .addEvtHandler(createdEvtHandler(agg))
    }

}
