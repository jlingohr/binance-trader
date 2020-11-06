package client.clients.rest.client

import java.time.Instant

import client.domain.AveragePrice
import client.domain.depths.depths.PartialDepthUpdate
import client.domain.depths.http.DepthLimit
import client.domain.depths.http.DepthLimit.Depth100
import client.domain.http.response.{BinanceResponse, Result}
import client.domain.klines.KlineInterval
import client.domain.klines.http.klines.Kline
import client.domain.params.TradeId
import client.domain.symbols.Symbol
import client.domain.trades.http.trades.{AggTrade, Trade}

trait MarketClient[F[_]] {
  def depth(symbol: Symbol, limit: DepthLimit = Depth100): F[BinanceResponse[Result[PartialDepthUpdate]]]

  def trades(symbol: Symbol, limit: Option[Int]): F[BinanceResponse[Result[Seq[Trade]]]]

  def historicalTrades(symbol: Symbol, limit: Option[Int], fromId: Option[TradeId]): F[BinanceResponse[Result[Trade]]]

  def aggTrades(symbol: Symbol,
                fromId: Option[TradeId],
                startTime: Option[Instant],
                endTime: Option[Instant],
                limit: Option[Int]): F[BinanceResponse[Result[Seq[AggTrade]]]]

  def klines(symbol: Symbol,
             interval: KlineInterval,
             startTime: Option[Instant],
             endTime: Option[Instant],
             limit: Int = 500): F[BinanceResponse[Result[Seq[Kline]]]]

  def avgPrice(symbol: Symbol): F[BinanceResponse[Result[AveragePrice]]]
}
