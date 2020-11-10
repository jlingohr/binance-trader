package client.domain.depths.http

import enumeratum.values.{IntEnum, IntEnumEntry}

sealed abstract class DepthLimit(override val value: Int) extends IntEnumEntry

object DepthLimit extends IntEnum[DepthLimit]{

  case object Depth5 extends DepthLimit(5)
  case object Depth10 extends DepthLimit(10)
  case object Depth20 extends DepthLimit(20)
  case object Depth50 extends DepthLimit(50)
  case object Depth100 extends DepthLimit(100)
  case object Depth500 extends DepthLimit(500)
  case object Depth1000 extends DepthLimit(1000)
  case object Depth5000 extends DepthLimit(5000)

  val values = findValues

}
