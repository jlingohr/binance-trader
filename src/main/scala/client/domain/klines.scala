package client.domain

import java.time.Instant

import client.domain.events.{AssetVolume, Price, TradeId}
import io.estatico.newtype.macros.newtype
import client.domain.symbols.Symbol


object klines {

  @newtype case class KlineInterval(value: String)
//  @newtype case object OneMinute extends KlineInterval("1m")
//  @newtype case object ThreeMinutes extends KlineInterval("3m")
//  @newtype case object FiveMinutes extends KlineInterval("5m")
//  @newtype case object FifteenMinutes extends KlineInterval("15m")
//  @newtype case object HalfHour extends KlineInterval("30m")
//  @newtype case object Hourly extends KlineInterval("1h")
//  @newtype case object TwoHourly extends KlineInterval("2h")
//  @newtype case object FourHourly extends KlineInterval("4h")
//  @newtype case object SixHourly extends KlineInterval("6h")
//  @newtype case object EightHourly extends KlineInterval("8h")
//  @newtype case object TwelveHourly extends KlineInterval("12h")
//  @newtype case object Daily extends KlineInterval(value = "1d")
//  @newtype case object ThreeDaily extends KlineInterval(value = "3d")
//  @newtype case object Weekly extends KlineInterval(value = "1w")
//  @newtype case object Monthly extends KlineInterval(value = "1M")

  case class Kline(startTime: Instant,
                  closeTime: Instant,
                  symbol: Symbol,
                  interval: KlineInterval,
                  firstTradeId: TradeId,
                  lastTradeId: TradeId,
                  openPrice: Price,
                  closePrice: Price,
                  highPrice: Price,
                  lowPrice: Price,
                  baseAssetVolume: AssetVolume,
                  numberOfTrades: Long,
                  isClosed: Boolean,
                  quoteAssetVolume: AssetVolume,
                  takerBuyBaseAssetVolume: AssetVolume,
                  takerBuyQuoteAssetVolume: AssetVolume) extends Event

  case class KlineEvent(eventTime: Instant,
                       symbol: Symbol,
                       kline: Kline) extends Event

}
