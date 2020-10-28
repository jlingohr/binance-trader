package client.clients

import java.time.Instant

import client.domain.{ Event, EventCategory}
import client.domain.depths.{Ask, Bid, DepthLevel, DepthUpdate, PartialBookDepth}
import client.domain.events.{AssetVolume, OrderId, Price, Quantity, TradeId, UpdateId}
import client.domain.klines.{Kline, KlineEvent, KlineInterval}
import client.domain.symbols.Symbol
import client.domain.tickers.{OrderBookUpdateId, SymbolBookTicker, SymbolMiniTicker, SymbolTicker}
import client.domain.trades.{AggTrade, Trade}
import io.circe.{Decoder, HCursor}

trait CirceJson {

  implicit val symbolDecoder: Decoder[Symbol] =
    Decoder.forProduct1("value")(Symbol.apply)

  implicit val tradeIdDecoder: Decoder[TradeId] =
    Decoder.forProduct1("value")(TradeId.apply)

  implicit val priceDecoder: Decoder[Price] =
    Decoder.forProduct1("value")(Price.apply)

  implicit val quantityDecoder: Decoder[Quantity] =
    Decoder.forProduct1("value")(Quantity.apply)

  implicit val OrderIdDecoder: Decoder[OrderId] =
    Decoder.forProduct1("value")(OrderId.apply)

  implicit val assetVolumeDecoder: Decoder[AssetVolume] =
    Decoder.forProduct1("value")(AssetVolume.apply)

  implicit val updateIdDecoder: Decoder[UpdateId] =
    Decoder.forProduct1("value")(UpdateId.apply)

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

  implicit val klineIntervalDecoder: Decoder[KlineInterval] =
    Decoder.forProduct1("value")(KlineInterval.apply)

  implicit val klineDecoder: Decoder[KlineEvent] = (c: HCursor) => {
    for {
      eventTime <- c.downField("E").as[Instant]
      k = c.downField("k")
      startTime <- k.downField("t").as[Instant]
      closeTime <- k.downField("T").as[Instant]
      symbol <- k.downField("s").as[Symbol]
      interval <- k.downField("i").as[KlineInterval]
      firstTradeId <- k.downField("f").as[TradeId]
      lastTradeId <- k.downField("L").as[TradeId]
      openPrice <- k.downField("o").as[Price]
      closePrice <- k.downField("c").as[Price]
      highPrice <- k.downField("h").as[Price]
      lowPrice <- k.downField("l").as[Price]
      baseAssetVolume <- k.downField("v").as[AssetVolume]
      numberOfTrades <- k.downField("n").as[Long]
      isThisKlineClosed <- k.downField("x").as[Boolean]
      quoteAssetVolume <- k.downField("q").as[AssetVolume]
      takerBuyBaseAssetVolume <- k.downField("V").as[AssetVolume]
      takerBuyQuoteAssetVolume <- k.downField("Q").as[AssetVolume]
    } yield KlineEvent(
      eventTime,
      symbol,
      Kline(
        startTime,
        closeTime,
        symbol,
        interval,
        firstTradeId,
        lastTradeId,
        openPrice,
        closePrice,
        highPrice,
        lowPrice,
        baseAssetVolume,
        numberOfTrades,
        isThisKlineClosed,
        quoteAssetVolume,
        takerBuyBaseAssetVolume,
        takerBuyQuoteAssetVolume)
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

  implicit val orderUpdateIdDecoder: Decoder[OrderBookUpdateId] =
    Decoder.forProduct1("value")(OrderBookUpdateId.apply)

  implicit val symbolBookTickerDecoder: Decoder[SymbolBookTicker] = Decoder.forProduct6(
    "u",
    "s",
    "b",
    "B",
    "a",
    "A"
  )(SymbolBookTicker.apply)

  implicit val depthLevelDecoder: Decoder[DepthLevel] = Decoder.forProduct1("value")(DepthLevel.apply)

  implicit val bidDecoder: Decoder[Bid] =
    Decoder.forProduct2("priceLevel", "quantity")(Bid.apply)

  implicit val askDecoder: Decoder[Ask] =
    Decoder.forProduct2("priceLevel", "quantity")(Ask.apply)

  implicit val partialBookDepthDecoder: Decoder[PartialBookDepth] = Decoder.forProduct3(
    "lastUpdateId",
    "bids",
    "asks"
  )(PartialBookDepth.apply)

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
      .orElse(c.as[PartialBookDepth])
  }

}
