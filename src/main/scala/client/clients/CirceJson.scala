package client.clients

import java.time.Instant

import cats.syntax.either._
import client.domain.depths._
import client.domain.events._
import client.domain.klines.{Kline, KlineEvent, KlineInterval}
import client.domain.symbols.Symbol
import client.domain.tickers.{OrderBookUpdateId, SymbolBookTicker, SymbolMiniTicker, SymbolTicker}
import client.domain.trades.{AggTrade, Trade}
import client.domain.{Event, EventCategory}
import io.circe.{Decoder, HCursor}
import io.estatico.newtype.Coercible
import io.estatico.newtype.ops._


trait CirceJson {

  implicit def coercibleDecoder[A: Coercible[B, *], B: Decoder]: Decoder[A] =
    Decoder[B].map(_.coerce[A])

  implicit val instantDecode: Decoder[Instant] = Decoder.decodeLong.emap { l =>
    Either.catchNonFatal(Instant.ofEpochMilli(l)).leftMap(e => s"Could not parse Instant: $e")
  }

  implicit val symbolDecoder: Decoder[Symbol] = Decoder[String].map(Symbol.apply)

  implicit val tradeIdDecoder: Decoder[TradeId] = Decoder[Long].map(TradeId.apply)

  implicit val priceDecoder: Decoder[Price] = Decoder[BigDecimal].map(Price.apply)

  implicit val quantityDecoder: Decoder[Quantity] = Decoder[BigDecimal].map(Quantity.apply)

  implicit val OrderIdDecoder: Decoder[OrderId] = Decoder[Long].map(OrderId.apply)

  implicit val assetVolumeDecoder: Decoder[AssetVolume] = Decoder[BigDecimal].map(AssetVolume.apply)

  implicit val updateIdDecoder: Decoder[UpdateId] = Decoder[Long].map(UpdateId.apply)

  implicit val aggTradeDecoder: Decoder[AggTrade] =
    Decoder
      .forProduct9(
        "E",
        "s",
        "a",
        "p",
        "q",
        "f",
        "l",
        "T",
        "m")(AggTrade.apply)

  implicit val tradeDecoder: Decoder[Trade] =
    Decoder
      .forProduct9(
        "E",
        "s",
        "t",
        "p",
        "q",
        "b",
        "a",
        "T",
        "m")(Trade.apply)

  implicit val klineIntervalDecoder: Decoder[KlineInterval] = Decoder[String].map(KlineInterval.apply)

  implicit val klineDecoder: Decoder[Kline] =
    Decoder.forProduct16(
      "t",
      "T",
      "s",
      "i",
      "f",
      "L",
      "o",
      "c",
      "h",
      "l",
      "v",
      "n",
      "x",
      "q",
      "V",
      "Q"
    )(Kline.apply)

  implicit val klineEventDecoder: Decoder[KlineEvent] = (c: HCursor) => {
    for {
      eventTime <- c.downField("E").as[Instant]
      symbol <- c.downField("s").as[Symbol]
      kline <- c.downField("k").as[Kline]
    } yield KlineEvent(
      eventTime,
      symbol,
      kline
    )
  }

  implicit val miniTickerDecoder: Decoder[SymbolMiniTicker] = Decoder.forProduct8(
    "E", "s", "c", "o", "h", "l", "v", "q"
  )(SymbolMiniTicker.apply)

  implicit val tickerDecoder: Decoder[SymbolTicker] = Decoder.forProduct22(
    "E",
    "s",
    "p",
    "P",
    "w",
    "x",
    "c",
    "Q",
    "b",
    "B",
    "a",
    "A",
    "o",
    "h",
    "l",
    "v",
    "q",
    "O",
    "C",
    "F",
    "L",
    "n"
  )(SymbolTicker.apply)

  implicit val orderUpdateIdDecoder: Decoder[OrderBookUpdateId] = Decoder[Long].map(OrderBookUpdateId.apply)

  implicit val symbolBookTickerDecoder: Decoder[SymbolBookTicker] = (c: HCursor) => {
    for {
      updateId <- c.downField("u").as[OrderBookUpdateId]
      symbol <- c.downField("s").as[Symbol]
      bestBidPrice <- c.downField("b").as[Price]
      bestBidQty <- c.downField("B").as[Quantity]
      bestAskPrice <- c.downField("a").as[Price]
      bestAskQty <- c.downField("A").as[Quantity]
    } yield(SymbolBookTicker(updateId, symbol, bestBidPrice, bestBidQty, bestAskPrice, bestAskQty))

  }

  implicit val depthLevelDecoder: Decoder[DepthLevel] = Decoder.forProduct1("value")(DepthLevel.apply)

  implicit val bidDecoder: Decoder[Bid] = (c: HCursor) =>
    for {
      f <- c.as[(Price, Quantity)]
    } yield Bid(f._1, f._2)

  implicit val askDecoder: Decoder[Ask] = (c: HCursor) =>
    for {
      f <- c.as[(Price, Quantity)]
    } yield Ask(f._1, f._2)

  implicit val partialBookDepthDecoder: Decoder[PartialBookDepth] = (c: HCursor) => {
    for {
      lastUpdateId <- c.downField("lastUpdateId").as[UpdateId]
      bids <- c.downField("bids").as[Seq[Bid]]
      asks <- c.downField("asks").as[Seq[Ask]]
    } yield PartialBookDepth(lastUpdateId, bids, asks)
  }


  implicit val depthUpdateDecoder: Decoder[DepthUpdate] = Decoder.forProduct6(
    "E",
    "s",
    "U",
    "u",
    "b",
    "a"
  )(DepthUpdate.apply)

  implicit val eventDecoder: Decoder[Event] = Decoder.instance { c =>
    c.downField("e")
      .as[EventCategory]
      .flatMap {
        case EventCategory.AggTrade => c.as[AggTrade]
        case EventCategory.Trade => c.as[Trade]
        case EventCategory.Ticker => c.as[SymbolTicker]
        case EventCategory.MiniTicker => c.as[SymbolMiniTicker]
        case EventCategory.Kline => c.as[KlineEvent]
        case EventCategory.Depth => c.as[DepthUpdate]
      }
      .orElse(c.as[SymbolBookTicker])
      .orElse(c.as[PartialBookDepth])
  }

}
