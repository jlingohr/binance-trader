package client.domain.ws

import enumeratum.{CirceEnum, Enum, EnumEntry}

trait Event

sealed abstract class EventCategory(override val entryName: String) extends EnumEntry

object EventCategory extends Enum[EventCategory] with CirceEnum[EventCategory] {

  case object AggTrade      extends EventCategory("aggTrade")
  case object Trade         extends EventCategory("trade")
  case object Kline         extends EventCategory("kline")
  case object Ticker        extends EventCategory("24hrTicker")
  case object MiniTicker    extends EventCategory("24hrMiniTicker")
  case object Depth         extends EventCategory("depthUpdate")
  case object OrderUpdate   extends EventCategory("executionReport")

  val values = findValues
}