package demo.pathdeptypes

import demo.common.{AggregateRef, Behavior}

import scala.collection.concurrent.{Map, TrieMap}
import scala.reflect.ClassTag

trait BackendDependentType[Agg] {

  implicit val aggregate: Aggregate[Agg]

  private var aggregateConfigs: Map[ClassTag[Agg], Behavior[Agg, aggregate.Cmd, aggregate.Evt]] =
    TrieMap()

  // format: off
  def configure(behavior: Behavior[Agg, aggregate.Cmd, aggregate.Evt])
               (implicit tag: ClassTag[Agg]) =
  aggregateConfigs += (tag -> behavior)

  def aggregateRef(implicit tag: ClassTag[Agg]): AggregateRef[Agg, aggregate.Cmd, aggregate.Evt] =
    new AggregateRef(aggregateConfigs(tag))

  // format: on
}
