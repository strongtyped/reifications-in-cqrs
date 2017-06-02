package demo.typeprojection

import demo.common.{ AggregateRef, Behavior }

import scala.collection.concurrent
import scala.reflect.ClassTag

object BackendTypeProj {

  def aggregateRef[A <: Aggregate](implicit tag: ClassTag[A]): AggregateRef[A, A#Command, A#Event] = {
    val behavior = aggregateConfigs(tag).asInstanceOf[Behavior[A, A#Command, A#Event]]
    new AggregateRef(behavior)
  }

  private var aggregateConfigs: concurrent.Map[ClassTag[_], Behavior[_, _, _]] = concurrent.TrieMap()

  def configure[A, C, E](behavior: Behavior[A, C, E])(implicit tag: ClassTag[A]) = {
    aggregateConfigs += (tag -> behavior)
    this
  }
  
}
