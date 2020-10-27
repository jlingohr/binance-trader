package client.domain

import eu.timepit.refined.types.string.NonEmptyString
import io.estatico.newtype.macros.newtype

trait Event

object events {

  @newtype case class Symbol(value: NonEmptyString)
  @newtype case class TradeId(value: Long)
  @newtype case class Price(value: BigDecimal)
  @newtype case class Quantity(value: NonEmptyString)
  @newtype case class OrderId(value: Long)
  @newtype case class AssetVolume(value: BigDecimal)
  @newtype case class UpdateId(value: Long)

}