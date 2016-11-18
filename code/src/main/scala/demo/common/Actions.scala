package demo.common

import demo.{ ClassTagExtractor, _ }

import scala.reflect.ClassTag

case class Actions[A, C, E](cmdHandlers: CommandHandler[C, E] = PartialFunction.empty,
                            evtHandlers: EventHandler[E, A]   = PartialFunction.empty) {

  def handleCommand(cmdHandler: CommandHandler[C, E]): Actions[A, C, E] =
    copy(cmdHandlers = cmdHandlers orElse cmdHandler)

  def handleEvent(evtHandler: EventHandler[E, A]): Actions[A, C, E] =
    copy(evtHandlers = evtHandlers orElse evtHandler)
}
