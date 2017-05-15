package demo

import demo.pathdeptypes.MainPathDependentTypes
import demo.typeparam.MainTypeParameters
import demo.typeprojection.MainTypeProjections

object Main {

  def main(args: Array[String]) = {

    MainTypeParameters.main(args)

    MainTypeProjections.main(args)

    MainPathDependentTypes.main(args)

  }
}
