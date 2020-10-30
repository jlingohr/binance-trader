package client.domain.orders.http

import java.time.Instant

import client.domain.orders.http.OrderResponse.OrderListId
import client.domain.params.{OrderId, Price, Quantity}
import client.domain.symbols.Symbol
import enumeratum.{CirceEnum, EnumEntry, Enum}

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

