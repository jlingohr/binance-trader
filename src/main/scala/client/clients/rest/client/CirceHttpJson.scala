package client.clients.rest.client

import client.clients.CirceJson
import client.domain.AveragePrice
import client.domain.account.http.Account.{AccountType, Balance, Permission}
import client.domain.depths.depths.{Ask, Bid, PartialDepthUpdate}
import client.domain.http.RateLimiter
import client.domain.klines.http.klines.Kline
import client.domain.orders.http.OCOOrderResponse.{CancelOCO, OCODetail, OCOOrder, OCOReport}
import client.domain.orders.http.OrderResponse._
import client.domain.params.UpdateId
import client.domain.tickers.http.tickers.{BookTicker, Ticker24Hr, TickerPrice}
import client.domain.trades.http.trades.{AggTrade, Trade}
import io.circe.generic.semiauto.deriveDecoder
import io.circe.{Decoder, HCursor}

trait CirceHttpJson extends CirceJson {

  implicit val orderListIdDecoder: Decoder[OrderListId] = Decoder[Long].map(OrderListId.apply)

  implicit val commissionAssetDecoder: Decoder[CommissionAsset] = Decoder[String].map(CommissionAsset.apply)

  implicit val depthDecoder: Decoder[PartialDepthUpdate] = (c: HCursor) => {
    for {
      lastUpdateId <- c.downField("lastUpdateId").as[UpdateId]
      bids <- c.downField("bids").as[Seq[Bid]]
      asks <- c.downField("asks").as[Seq[Ask]]
    } yield PartialDepthUpdate(lastUpdateId, bids, asks)
  }

  implicit val tradeDecoder: Decoder[Trade] = deriveDecoder[Trade]

  implicit val aggTradeDecoder: Decoder[AggTrade] = deriveDecoder[AggTrade]

  implicit val klineDecoder: Decoder[Kline] = deriveDecoder[Kline]

  implicit val avgPriceDecoder: Decoder[AveragePrice] = deriveDecoder[AveragePrice]

  implicit val ticker24HrDecoder: Decoder[Ticker24Hr] = deriveDecoder[Ticker24Hr]

  implicit val tickerPriceDecoder: Decoder[TickerPrice] = deriveDecoder[TickerPrice]

  implicit val bookTickerDecoder: Decoder[BookTicker] = deriveDecoder[BookTicker]

  implicit val orderAckDecoder: Decoder[OrderAck] = deriveDecoder[OrderAck]

  implicit val orderResultOnlyDecoder: Decoder[Result] = Decoder.forProduct8(
    "price",
    "origQty",
    "executedQty",
    "cummulativeQuoteQty",
    "status",
    "timeInForce",
    "type",
    "side"
  )(Result.apply)

  implicit val fillDecoder: Decoder[Fill] = deriveDecoder[Fill]

  implicit val orderFullDecoder: Decoder[OrderFull] = (c: HCursor) => {
    for {
      ack <- orderAckDecoder.apply(c)
      result <- orderResultOnlyDecoder.apply(c)
      fills <- c
        .downField("fills")
        .success
        .map(_.as[Seq[Fill]])
        .getOrElse(Right(Seq.empty[Fill]))
    } yield OrderFull(ack, result, fills)
  }

  implicit val orderDecoder: Decoder[Order] = deriveDecoder[Order]

  implicit val cancelOrderDecoder: Decoder[CancelOrder] = deriveDecoder

  implicit val openOrderDecoder: Decoder[OpenOrder] = deriveDecoder

  implicit val ocoOrderDetailDecoder: Decoder[OCODetail] = deriveDecoder

  implicit val ocoOrderReport: Decoder[OCOReport] = deriveDecoder

  implicit val ocoOrderDecoder: Decoder[OCOOrder] = deriveDecoder

  implicit val ocoCancelDecoder: Decoder[CancelOCO] = deriveDecoder

  implicit val accountTypeDecoder: Decoder[AccountType] = Decoder[String].map(AccountType.apply)

  implicit val permissionDecoder: Decoder[Permission] = Decoder[String].map(Permission.apply)

  implicit val balanceDecoder: Decoder[Balance] = deriveDecoder

  implicit val rateLimitDecoder: Decoder[RateLimiter] = deriveDecoder

}
