package client.clients.rest.client

import java.time.Instant

import client.domain.http.response
import client.domain.orders.http.OrderResponse.OrderListId
import client.domain.orders.http.{OCOOptions, Side}
import client.domain.params.{OrderId, Price, Quantity}
import client.domain.symbols.Symbol
import eu.timepit.refined.numeric.LessEqual

trait OCOClient[F[_]] {

  def order(symbol: Symbol,
            side: Side,
            quantity: Quantity,
            price: Price,
            stopPrice: Price,
            timestamp: Instant,
            options: OCOOptions): F[response]

  def cancel(symbol: Symbol,
             orderListId: Option[OrderListId],
             listClientOrderId: Option[String],
             newClientOrderId: Option[OrderId],
             recvWindow: Option[LessEqual[60000]],
             timestamp: Instant): F[response]

  def orderList(orderListId: Option[OrderListId],
                origClientOrderId: Option[OrderId],
                recvWindow: Option[LessEqual[60000]],
                timestamp: Instant): F[response]

  def allOrderList(frinId: Option[OrderId],
                   startTime: Option[Instant],
                   endTime: Option[Instant],
                   limit: Option[LessEqual[1000]],
                   recvWindow: Option[LessEqual[60000]],
                   timestamp: Instant): F[response]

  def openOrderList(recvWindow: Option[Long], timestamp: Instant): F[response]

}
