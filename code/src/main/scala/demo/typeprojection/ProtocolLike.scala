package demo.typeprojection

import demo.common.Actions

trait ProtocolLike {
  type Command
  type Event
}
