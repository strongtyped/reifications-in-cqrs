package demo.pathdeptypes

import demo.common.{ AggregateRef, Behavior }

import scala.collection.concurrent
import scala.reflect.ClassTag

object BackendDependentType {

  private var aggregateConfigs: concurrent.Map[ClassTag[_], Behavior[_, _, _]] = concurrent.TrieMap()

  // format: off
  def configure[A, C, E](behavior: Behavior[A, C, E])
                           (implicit tag: ClassTag[A]) = {

    aggregateConfigs += (tag -> behavior)
    this
  }

  def aggregateRef[A](implicit types: Types[A], tag: ClassTag[A]): AggregateRef[A, types.Command, types.Event] = {
    val behavior = aggregateConfigs(tag).asInstanceOf[Behavior[A, types.Command, types.Event]]
    new AggregateRef(behavior)
  }
  // format: on
}
