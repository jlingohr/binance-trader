package client.domain

import java.time.Instant

import client.domain.events.{AssetVolume, Price, Quantity, TradeId}
import client.domain.symbols.Symbol
import io.estatico.newtype.macros.newtype


object tickers {

  case class SymbolMiniTicker(eventTime: Instant,
                       symbol: Symbol,
                       closePrice: Price,
                       openPrice: Price,
                       highPrice: Price,
                       lowPrice: Price,
                       totalTradedBaseAssetVolume: AssetVolume,
                       totalTradedQuoteAssetVolume: AssetVolume) extends Event

  case class SymbolTicker(eventTime: Instant,
                         symbol: Symbol,
                         priceChange: Price,
                         priceChangePercent: BigDecimal,
                         weightedAveragePrice: Price,
                         firstTradePrice: Price,
                         lastPrice: Price,
                         lastQuantity: Quantity,
                         bestBidPrice: Price,
                         bestBidQuantity: Quantity,
                         bestAskPrice: Price,
                         bestAskQuantity: Quantity,
                         openPrice: Price,
                         highPrice: Price,
                         lowPrice: Price,
                         totalTradedBaseAssetVolume: AssetVolume,
                         totalTradedQuoteAssetVolume: AssetVolume,
                         statisticsOpenTime: Instant,
                         statisticsCloseTime: Instant,
                         firstTradeId: TradeId,
                         lastTradeId: TradeId,
                         numberOfTrades: Long) extends Event

  @newtype case class OrderBookUpdateId(value: Long)

  case class SymbolBookTicker(updateId: OrderBookUpdateId,
                             symbol: Symbol,
                             bestBidPrice: Price,
                             bestBitQuantity: Quantity,
                             bestAskPrice: Price,
                             bestAskQuantity: Quantity) extends Event

}
