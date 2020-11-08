package client.clients.rest.client

import java.time.Instant

import client.domain.account.http.AccountResponse.AccountInformation
import client.domain.http.response.{BinanceResponse, Result}
import client.domain.params.TradeId
import client.domain.symbols.Symbol
import client.domain.trades.http.trades.Trade

trait AccountClient[F[_]] {

  def account(recvWindow: Option[Long],
              timestamp: Instant): F[BinanceResponse[Result[AccountInformation]]]

  def myTrades(symbol: Symbol,
               startTime: Option[Instant],
               endTime: Option[Instant],
               fromId: Option[TradeId],
               limit: Option[Int],
               recvWindow: Option[Long],
               timestamp: Instant): F[BinanceResponse[Result[Seq[Trade]]]]

}
