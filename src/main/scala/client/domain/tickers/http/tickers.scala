package client.domain.tickers.http

import java.time.Instant

import client.domain.params.{AssetVolume, ChangePercent, Price, Quantity, TradeId}
import client.domain.symbols.Symbol

object tickers {

  case class Ticker24Hr(symbol: Symbol,
                        priceChange: Price,
                        priceChangedPercent: ChangePercent,
                        weightedAvgPrice: Price,
                        prevClosePrice: Price,
                        lastPrice: Price,
                        lastQty: Quantity,
                        bidPrice: Price,
                        askPrice: Price,
                        openPrice: Price,
                        highPrice: Price,
                        lowPrice: Price,
                        volume: AssetVolume,
                        quoteVolume: AssetVolume,
                        openTime: Instant,
                        closeTime: Instant,
                        firstId: TradeId,
                        lastId: TradeId,
                        count: Int)

  case class TickerPrice(symbol: Symbol, price: Price)

  case class BookTicker(symbol: Symbol, bidPrice: Price, bidQty: Quantity, askPrice: Price, askQty: Quantity)

}
