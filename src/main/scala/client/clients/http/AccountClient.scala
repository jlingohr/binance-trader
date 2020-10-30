package client.clients.http

import java.time.Instant

import client.domain.params.TradeId
import client.domain.http.BinanceResponse
import client.domain.symbols.Symbol
import eu.timepit.refined.numeric.LessEqual

trait AccountClient[F[_]] {

  def account(symbol: Symbol, timestamp: Instant): F[BinanceResponse]

  def myTrades(symbol: Symbol,
               startTime: Option[Instant],
               endTime: Option[Instant],
              fromId: Option[TradeId],
              limit: Option[LessEqual[1000]],
              recvWindow: Option[LessEqual[60000]],
              timestamp: Instant): F[BinanceResponse]

}
