package client.clients

import java.time.Instant

import client.domain.depths.depths.{Ask, Bid}
import client.domain.params.{Asset, AssetVolume, ChangePercent, ClientOrderId, OrderId, Price, Quantity, TradeId, UpdateId}
import client.domain.symbols.Symbol
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder, HCursor}

trait CirceJson {

  implicit val symbolDecoder: Decoder[Symbol] = Decoder[String].map(Symbol.apply)

  implicit val tradeIdDecoder: Decoder[TradeId] = Decoder[Long].map(TradeId.apply)

  implicit val priceDecoder: Decoder[Price] = Decoder[BigDecimal].map(Price.apply)

  implicit val quantityDecoder: Decoder[Quantity] = Decoder[BigDecimal].map(Quantity.apply)

  implicit val OrderIdDecoder: Decoder[OrderId] = Decoder[Long].map(OrderId.apply)

  implicit val assetDecoder: Decoder[Asset] = assetDecoder

  implicit val assetVolumeDecoder: Decoder[AssetVolume] = Decoder[BigDecimal].map(AssetVolume.apply)

  implicit val updateIdDecoder: Decoder[UpdateId] = Decoder[Long].map(UpdateId.apply)

  implicit val bidDecoder: Decoder[Bid] = (c: HCursor) =>
    for {
      f <- c.as[(Price, Quantity)]
    } yield Bid(f._1, f._2)

  implicit val askDecoder: Decoder[Ask] = (c: HCursor) =>
    for {
      f <- c.as[(Price, Quantity)]
    } yield Ask(f._1, f._2)

  implicit val changePercentDecoder: Decoder[ChangePercent] = Decoder[BigDecimal].map(ChangePercent.apply)

  implicit val clientOrderIdDecoder: Decoder[ClientOrderId] = Decoder[String].map(ClientOrderId.apply)

  implicit val instantDecode: Decoder[Instant] = deriveDecoder[Instant]
  implicit val instantEncoder: Encoder[Instant] = deriveEncoder[Instant]

}
