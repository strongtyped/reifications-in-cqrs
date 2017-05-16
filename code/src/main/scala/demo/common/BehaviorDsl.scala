package demo.common

object BehaviorDsl {

  type StateToActions[A, C, E] = PartialFunction[A, Actions[A, C, E]]

  def first[A, C, E](onCreation: => Actions[A, C, E]) =
    new BehaviorStage(onCreation)

  class BehaviorStage[A, C, E](onCreation: => Actions[A, C, E]) {

    def andThen(postCreation: StateToActions[A, C, E]): Behavior[A, C, E] = {
      new Behavior[A, C, E] {

        override def onCommand(aggState: Option[A], cmd: C): List[E] = {
          aggState
            .map { agg =>
              postCreation(agg).cmdHandlers(cmd)
            }
            .getOrElse { onCreation.cmdHandlers(cmd) }

        }

        override def onEvent(aggState: Option[A], evt: E): Option[A] = {
          aggState
            .map { agg =>
              postCreation(agg).evtHandlers(evt)
            }
            .orElse { Option(onCreation.evtHandlers(evt)) }
        }
      }
    }
  }

}
