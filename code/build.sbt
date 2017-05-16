name := """reification"""

version := "1.0"

scalaVersion := "2.12.0"

addCommandAlias("runAll", "runMain demo.Main")
addCommandAlias("runTypeParams", "runMain demo.typeparam.MainTypeParameters")
addCommandAlias("runTypeProjections", "runMain demo.typeprojection.MainTypeProjections")
addCommandAlias("runPathDepTypes", "runMain demo.pathdeptypes.MainPathDependentTypes")
