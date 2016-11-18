package demo

import demo.pathdeptypes.MainPathDependentType
import demo.typeparam.MainTypeParameters
import demo.typeprojection.MainTypeProjection

object Main extends App {

  MainTypeParameters.app()

  MainTypeProjection.app()

  MainPathDependentType.app()

}
