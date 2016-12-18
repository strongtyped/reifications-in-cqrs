package demo.pathdeptypes

import demo.common.Behavior

trait Types[Agg] {
  type Cmd
  type Evt
}

trait Aggregate[Agg] extends Types[Agg] {
  def behavior: Behavior[Agg, Cmd, Evt]
}
