package demo.pathdeptypes

import demo.common.{AggregateRef, Behavior}

import scala.collection.concurrent.{Map, TrieMap}
import scala.reflect.ClassTag

trait BackendDependentType[A] {

  implicit val aggregate: Aggregate[A]

  private var aggregateConfigs: Map[ClassTag[A], Behavior[A, aggregate.Cmd, aggregate.Evt]] =
    TrieMap()

  // format: off
  def configure(behavior: Behavior[A, aggregate.Cmd, aggregate.Evt])
               (implicit tag: ClassTag[A]) =
  aggregateConfigs += (tag -> behavior)

  def aggregateRef(implicit tag: ClassTag[A]): AggregateRef[A, aggregate.Cmd, aggregate.Evt] =
    new AggregateRef(aggregateConfigs(tag))

  // format: on
}
