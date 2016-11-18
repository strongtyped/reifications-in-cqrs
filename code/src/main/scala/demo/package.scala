package object demo {

  type CommandHandler[C, E] = PartialFunction[C, List[E]]
  type EventHandler[E, A]   = PartialFunction[E, A]

}
