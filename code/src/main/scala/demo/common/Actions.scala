package demo.common

import demo._

case class Actions[A, C, E](cmdHandlers: CommandHandler[C, E] = PartialFunction.empty,
                            evtHandlers: EventHandler[E, A]   = PartialFunction.empty) {

  def commandHandler(cmdHandler: CommandHandler[C, E]): Actions[A, C, E] =
    copy(cmdHandlers = cmdHandlers orElse cmdHandler)

  def eventHandler(evtHandler: EventHandler[E, A]): Actions[A, C, E] =
    copy(evtHandlers = evtHandlers orElse evtHandler)
}
