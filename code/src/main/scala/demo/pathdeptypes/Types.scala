package demo.pathdeptypes

import demo.common.Actions

trait Types[A] {

  type Command
  type Event

  val actions = Actions[A, Command, Event]()

  implicit def self: this.type = this
}
