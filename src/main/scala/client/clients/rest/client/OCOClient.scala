package client.clients.rest.client

import java.time.Instant

import client.domain.http.response.{BinanceResponse, Result}
import client.domain.orders.http.OCOOrderResponse.{CancelOCO, OCOOrder}
import client.domain.orders.http.OrderResponse.OrderListId
import client.domain.orders.http.{Side, TimeInForce}
import client.domain.params.{OrderId, Price, Quantity}
import client.domain.symbols.Symbol

trait OCOClient[F[_]] {

  def order(symbol: Symbol,
            side: Side,
            quantity: Quantity,
            price: Price,
            stopPrice: Price,
            timestamp: Instant,
            listClientOrderId: Option[OrderId],
            limitClientOrderId: Option[OrderId],
            limitIcebergQty: Option[Quantity],
            stopClientOrderId: Option[OrderId],
            stopLimitPrice: Option[Price],
            stopIcebergQty: Option[Quantity],
            stopLimitTimeInForce: Option[TimeInForce],
            recvWindow: Option[Long]): F[BinanceResponse[Result[OCOOrder]]]

  def cancel(symbol: Symbol,
             orderListId: Option[OrderListId],
             listClientOrderId: Option[String],
             newClientOrderId: Option[OrderId],
             recvWindow: Option[Long],
             timestamp: Instant): F[BinanceResponse[Result[CancelOCO]]]

  def orderList(orderListId: Option[OrderListId],
                origClientOrderId: Option[OrderId],
                recvWindow: Option[Long],
                timestamp: Instant): F[BinanceResponse[Result[OCOOrder]]]

  def allOrderList(frinId: Option[OrderId],
                   startTime: Option[Instant],
                   endTime: Option[Instant],
                   limit: Option[Int],
                   recvWindow: Option[Long],
                   timestamp: Instant): F[BinanceResponse[Result[Seq[OCOOrder]]]]

  def openOrderList(recvWindow: Option[Long], timestamp: Instant): F[BinanceResponse[Result[Seq[OCOOrder]]]]

}
