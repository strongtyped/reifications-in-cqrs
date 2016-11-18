package demo.pathdeptypes

import demo.common.{ AggregateRef, Behavior }

import scala.collection.concurrent
import scala.reflect.ClassTag

object BackendDependentType {

  private var aggregateConfigs: concurrent.Map[ClassTag[_], Behavior[_, _, _]] = concurrent.TrieMap()

  // format: off
  def configure[T <: Types](types: T)
                           (behavior: Behavior[types.Aggregate, types.Command, types.Event])
                           (implicit tag: ClassTag[T]) = {

    aggregateConfigs += (tag -> behavior)
    this
  }

  def aggregateRef[T <: Types](types: T)
                              (implicit tag: ClassTag[T]): AggregateRef[types.Aggregate, types.Command, types.Event] = {

    val behavior = aggregateConfigs(tag).asInstanceOf[Behavior[types.Aggregate, types.Command, types.Event]]
    new AggregateRef(behavior)
  }

  // format: on
}
