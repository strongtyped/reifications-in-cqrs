package demo.pathdeptypes

import demo.common.Actions

trait Types {

  type Aggregate
  type Command
  type Event

  val actions = Actions[Aggregate, Command, Event]()

  val types: this.type = this

}
