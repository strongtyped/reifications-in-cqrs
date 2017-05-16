package demo.common

trait Behavior[A, C, E] {

  def onCommand(aggState: Option[A], cmd: C): List[E]

  def onEvent(aggState: Option[A], evt: E): Option[A]

}
