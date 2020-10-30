package client.clients.http

import java.time.Instant

import client.domain.depths.http.DepthLimit
import client.domain.depths.http.DepthLimit.Depth100
import client.domain.params.TradeId
import client.domain.http.BinanceResponse
import client.domain.klines.ws.klines.KlineInterval
import client.domain.symbols.Symbol
import eu.timepit.refined.numeric.LessEqual


trait MarketClient[F[_]] {
  def depth(symbol: Symbol, limit: DepthLimit = Depth100): F[BinanceResponse]

  def trades(symbol: Symbol, limit: Option[LessEqual[1000]]): F[BinanceResponse]

  def historicalTrades(symbol: Symbol, limit: Option[LessEqual[1000]], fromId: Option[TradeId]): F[BinanceResponse]

  def aggTrades(symbol: Symbol,
                fromId: Option[TradeId],
                startTime: Option[Instant],
                endTime: Option[Instant],
                limit: Option[LessEqual[1000]]): F[BinanceResponse]

  def klines(symbol: Symbol,
             interval: KlineInterval,
             startTime: Option[Instant],
             endTime: Option[Instant],
             limit: Option[LessEqual[1000]]): F[BinanceResponse]

  def avgPrice(symbol: Symbol): F[BinanceResponse]
}
