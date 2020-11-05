package client.clients.rest.client

import java.time.Instant

import client.domain.orders.http.OrderResponse.{CancelOrder, Order, OrderFull}
import client.domain.orders.http.{OrderOptions, OrderType, Side}
import client.domain.params.OrderId
import client.domain.symbols.Symbol

trait OrderClient[F[_]] {

  def order(symbol: Symbol,
            side: Side,
            orderType: OrderType,
            timestamp: Instant,
            options: OrderOptions): F[OrderFull]

  def test(symbol: Symbol,
           side: Side,
           orderType: OrderType,
           timestamp: Instant,
           options: OrderOptions): F[Unit]

  def orderStatus(symbol: Symbol,
                 orderId: Option[OrderId],
                 origClientOrderId: Option[OrderId],
                 recvWindow: Option[Long],
                 timestamp: Instant): F[Order]

  def cancel(symbol: Symbol,
             orderId: Option[OrderId],
             origClientOrderId: Option[OrderId],
             newClientOrderId: Option[OrderId],
             recvWindow: Option[Long],
             timestamp: Instant): F[CancelOrder]

  def cancelAll(symbol: Symbol,
                recvWindow: Option[Long],
                timestamp: Instant): F[Seq[CancelOrder]]

  def currentOpenOrders(symbol: Symbol,
                       recvWindow: Option[Long],
                       timestamp: Instant): F[Seq[Order]]

  def allOrders(symbol: Symbol,
               orderId: Option[OrderId],
               startTime: Option[Instant],
               endTime: Option[Instant],
               limit: Option[Int],
               recvWindow: Option[Long],
               timestamp: Instant): F[Seq[Order]]

}
