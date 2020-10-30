package client.domain.orders.http

import enumeratum.{CirceEnum, Enum, EnumEntry}
import eu.timepit.refined.api.Refined
import eu.timepit.refined.string.MatchesRegex
import io.estatico.newtype.macros.newtype

sealed trait OrderType extends EnumEntry

object OrderType extends Enum[OrderType] with CirceEnum[OrderType] {

  final case object LIMIT extends OrderType
  final case object MARKET extends OrderType
  final case object STOP_LOSS extends OrderType
  final case object STOP_LOSS_LIMIT extends OrderType
  final case object TAKE_PROFIT extends OrderType
  final case object TAKE_PROFIT_LIMIT extends OrderType
  final case object LIMIT_MAKER extends OrderType

  val values = findValues

}

sealed trait NewOrderRespType extends EnumEntry

object NewOrderRespType extends Enum[NewOrderRespType] with CirceEnum[NewOrderRespType] {
  final case object ACK extends NewOrderRespType
  final case object RESULT extends NewOrderRespType
  final case object FULL extends NewOrderRespType

  val values = findValues
}

sealed trait Side extends EnumEntry

object Side extends Enum[Side] with CirceEnum[Side]  {
  final case object BUY extends Side
  final case object SELL extends Side

  val values = findValues
}

sealed trait TimeInForce extends EnumEntry

object TimeInForce extends Enum[TimeInForce] with CirceEnum[TimeInForce] {
  final case object GTC extends TimeInForce
  final case object IDC extends TimeInForce
  final case object FOK extends TimeInForce

  override def values = findValues
}
