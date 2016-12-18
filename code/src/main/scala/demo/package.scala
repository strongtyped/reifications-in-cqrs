package object demo {

  type CmdHandler[Cmd, Evt] = PartialFunction[Cmd, List[Evt]]
  type EvtHandler[Evt, Agg] = PartialFunction[Evt, Agg]

  def uncurry[Z, Y, X](z_2_y2x: Function[Z, Function[Y, X]]): Function2[Z, Y, X] =
    (z, y) => z_2_y2x(z)(y)

}
