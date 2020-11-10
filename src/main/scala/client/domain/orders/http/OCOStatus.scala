package client.domain.orders.http

import enumeratum.{CirceEnum, Enum, EnumEntry}

sealed trait ListStatusType extends EnumEntry

object ListStatusType extends Enum[ListStatusType] with CirceEnum[ListStatusType] {

  final case object RESPONSE extends ListStatusType
  final case object EXEC_STARTED extends ListStatusType
  final case object ALL_DONE extends ListStatusType

  val values = findValues

}

sealed trait ListOrderStatus extends EnumEntry

object ListOrderStatus extends Enum[ListOrderStatus] with CirceEnum[ListOrderStatus] {
  final case object EXECUTING extends ListOrderStatus
  final case object ALL_DONE extends ListOrderStatus
  final case object REJECT extends ListOrderStatus

  val values = findValues
}

sealed trait ContingencyType extends EnumEntry

object ContingencyType extends Enum[ContingencyType] with CirceEnum[ContingencyType] {
  final case object OCO extends ContingencyType

  val values = findValues
}