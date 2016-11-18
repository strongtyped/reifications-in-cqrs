package demo.typeparam

import demo.common.{ AggregateRef, Behavior }

import scala.collection.concurrent
import scala.reflect.ClassTag

object BackendParam {

  private var aggregateConfigs: concurrent.Map[ClassTag[_], Behavior[_, _, _]] = concurrent.TrieMap()

  def configure[A, C, E](behavior: Behavior[A, C, E])(implicit tag: ClassTag[A]) = {
    aggregateConfigs += (tag -> behavior)
    this
  }

  def aggregateRef[A, C, E](implicit tag: ClassTag[A]): AggregateRef[A, C, E] = {
    val behavior = aggregateConfigs(tag).asInstanceOf[Behavior[A, C, E]]
    new AggregateRef(behavior)
  }

}
