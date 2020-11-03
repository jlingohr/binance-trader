package client.clients.rest.client

import java.time.Instant

import client.clients.rest.BinanceRest
import client.domain.AveragePrice
import client.domain.depths.depths.PartialDepthUpdate
import client.domain.depths.http.DepthLimit
import client.domain.depths.http.DepthLimit.Depth100
import client.domain.http.BinanceResponse
import client.domain.klines.KlineInterval
import client.domain.klines.http.klines.Kline
import client.domain.params.TradeId
import client.domain.symbols.Symbol
import client.domain.trades.http.trades.{AggTrade, Trade}
import eu.timepit.refined.numeric.LessEqual

trait MarketClient[F[_]] extends BinanceRest[F] {
  def depth(symbol: Symbol, limit: DepthLimit = Depth100): F[PartialDepthUpdate]

  def trades(symbol: Symbol, limit: Option[Int]): F[Seq[Trade]]

  def historicalTrades(symbol: Symbol, limit: Option[Int], fromId: Option[TradeId]): F[BinanceResponse]

  def aggTrades(symbol: Symbol,
                fromId: Option[TradeId],
                startTime: Option[Instant],
                endTime: Option[Instant],
                limit: Option[Int]): F[Seq[AggTrade]]

  def klines(symbol: Symbol,
             interval: KlineInterval,
             startTime: Option[Instant],
             endTime: Option[Instant],
             limit: Option[Int]): F[Seq[Kline]]

  def avgPrice(symbol: Symbol): F[AveragePrice]
}
