package demo.common

object BehaviorDsl {

  type StateToActions[A, C, E] = PartialFunction[A, Actions[A, C, E]]

  def first[A, C, E](onCreation: => Actions[A, C, E]) =
    new BehaviorStage(onCreation)

  class BehaviorStage[A, C, E](onCreation: => Actions[A, C, E]) {

    def andThen(postCreation: StateToActions[A, C, E]): Behavior[A, C, E] = {
      new Behavior[A, C, E] {
        def onCommand(cmd: C): List[E]         = onCreation.cmdHandlers(cmd)
        def onEvent(evt: E): A                 = onCreation.evtHandlers(evt)
        def onCommand(agg: A, cmd: C): List[E] = postCreation(agg).cmdHandlers(cmd)
        def onEvent(agg: A, evt: E): A         = postCreation(agg).evtHandlers(evt)
      }
    }
  }

}
