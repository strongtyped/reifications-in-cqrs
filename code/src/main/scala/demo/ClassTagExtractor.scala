package demo

import scala.reflect.ClassTag

abstract class ClassTagExtractor[T: ClassTag] {

  def unapply(obj: T): Option[T] = {
    val tag = implicitly[ClassTag[T]]
    // need classTag because of erasure as we must be able to find back the original type
    if (obj.getClass == tag.runtimeClass) Some(obj)
    else None
  }
}
