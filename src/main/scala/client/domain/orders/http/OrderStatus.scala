package client.domain.orders.http

import enumeratum.{CirceEnum, Enum, EnumEntry}

sealed trait OrderStatus extends EnumEntry

object OrderStatus extends Enum[OrderStatus] with CirceEnum[OrderStatus] {

  final case object NEW extends OrderStatus
  final case object PARTIALLY_FILLED extends OrderStatus
  final case object CANCELED extends OrderStatus
  final case object PENDING_CANCEL extends OrderStatus
  final case object REJECTED extends OrderStatus
  final case object EXPIRED extends OrderStatus

  val values = findValues

}

