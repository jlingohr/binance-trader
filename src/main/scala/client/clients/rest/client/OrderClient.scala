package client.clients.rest.client

import java.time.Instant

import client.domain.http.BinanceResponse
import client.domain.orders.http.{OrderOptions, OrderType, Side}
import client.domain.params.OrderId
import client.domain.symbols.Symbol
import eu.timepit.refined.numeric.LessEqual

trait OrderClient[F[_]] {

  def order(symbol: Symbol, side: Side, orderType: OrderType, options: OrderOptions): F[BinanceResponse]

  def test(symbol: Symbol, side: Side, orderType: OrderType, options: OrderOptions): F[BinanceResponse]

  def cancel(symbol: Symbol,
             orderId: Option[OrderId],
             origClientOrderId: Option[OrderId],
             newClientOrderId: Option[OrderId],
             recvWindow: Option[LessEqual[60000]],
             timestamp: Instant): F[BinanceResponse]

  def cancelAll(symbol: Symbol,
                recvWindow: Option[LessEqual[60000]],
                timestamp: Instant): F[BinanceResponse]

}
