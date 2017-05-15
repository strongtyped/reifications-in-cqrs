package demo.typeprojection

import demo.common.{ AggregateRef, Behavior }

import scala.collection.concurrent
import scala.reflect.ClassTag

object BackendTypeProj {

  private var aggregateConfigs: concurrent.Map[ClassTag[_], Behavior[_, _, _]] = concurrent.TrieMap()

  // format: off
  def configure[A, C, E](behavior: Behavior[A, C, E])(implicit tag: ClassTag[A]) = {
    aggregateConfigs += (tag -> behavior)
    this
  }

  def aggregateRef[A <: Aggregate](implicit tag: ClassTag[A]): AggregateRef[A, A#Protocol#Command, A#Protocol#Event] = {
    val behavior = aggregateConfigs(tag).asInstanceOf[Behavior[A, A#Protocol#Command, A#Protocol#Event]]
    new AggregateRef(behavior)
  }
  // format: on
}
