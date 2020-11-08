package client.domain

import io.estatico.newtype.macros.newtype

object params {

  @newtype case class TradeId(value: Long)

  @newtype case class Price(value: BigDecimal)

  @newtype case class Quantity(value: BigDecimal)

  @newtype case class OrderId(value: Long)

  @newtype case class ClientOrderId(value: String)

  @newtype case class AssetVolume(value: BigDecimal)

  @newtype case class UpdateId(value: Long)

  @newtype case class ChangePercent(value: BigDecimal)

  @newtype case class Commission(value: Long)

  @newtype case class Asset(value: String)

  @newtype case class Permission(value: String)

}
