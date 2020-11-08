package client.clients.rest.client.interpreter

import java.time.Instant

import cats.effect.Sync
import cats.implicits._
import client.algebra.SecurityService
import client.clients.rest.BinanceRestEndpoint
import client.clients.rest.client.{CirceHttpJson, OCOClient}
import client.domain.http.response
import client.domain.http.response.{BinanceResponse, Result}
import client.domain.orders.http.OCOOrderResponse.{CancelOCO, OCOOrder}
import client.domain.orders.http.OrderResponse.OrderListId
import client.domain.orders.http.{OCOOrderResponse, Side, TimeInForce}
import client.domain.params._
import client.domain.symbols.Symbol
import client.effects.effects.MonadThrow
import org.http4s.circe.JsonDecoder
import org.http4s.client.Client
import org.http4s.dsl.Http4sDsl
import org.http4s.{Request, Uri}


class LiveOCOClient[F[_]: Sync: JsonDecoder: MonadThrow] (client: Client[F], security: SecurityService)
  extends OCOClient[F]
    with Http4sDsl[F]
    with CirceHttpJson {

  import OCOClientRequests._

  override def order(symbol: Symbol,
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
                     recvWindow: Option[Long]): F[BinanceResponse[Result[OCOOrder]]] =
    orderRequest(
      symbol,
      side,
      quantity,
      price,
      stopPrice,
      timestamp,
      listClientOrderId,
      limitClientOrderId,
      limitIcebergQty,
      stopClientOrderId,
      stopLimitPrice,
      stopIcebergQty,
      stopLimitTimeInForce,
      recvWindow
    )
      .liftTo[F]
      .map(security.putHeader)
      .map(security.sign)
      .flatMap { uri =>
        client.fetch(Request[F](POST, uri)) { r =>
          BinanceResponse.create[F, OCOOrder](r)
        }
      }

  override def cancel(symbol: Symbol,
                      orderListId: Option[OrderListId],
                      listClientOrderId: Option[String],
                      newClientOrderId: Option[OrderId],
                      recvWindow: Option[Long],
                      timestamp: Instant): F[response.BinanceResponse[Result[CancelOCO]]] =
    cancelOrderRequest(symbol, orderListId, listClientOrderId, newClientOrderId, recvWindow, timestamp)
      .liftTo[F]
      .map(security.putHeader)
      .map(security.sign)
      .flatMap { uri =>
        client.fetch(Request[F](DELETE, uri)) { r =>
          BinanceResponse.create[F, CancelOCO](r)
        }
      }

  override def orderList(orderListId: Option[OrderListId],
                         origClientOrderId: Option[OrderId],
                         recvWindow: Option[Long],
                         timestamp: Instant): F[response.BinanceResponse[Result[OCOOrder]]] =
    orderListRequest(orderListId, origClientOrderId, recvWindow, timestamp)
      .liftTo[F]
      .map(security.putHeader)
      .map(security.sign)
      .flatMap { uri =>
        client.fetch(Request[F](GET, uri)) { r =>
          BinanceResponse.create[F, OCOOrder](r)
        }
      }

  override def allOrderList(fromId: Option[OrderId],
                            startTime: Option[Instant],
                            endTime: Option[Instant],
                            limit: Option[Int],
                            recvWindow: Option[Long],
                            timestamp: Instant): F[response.BinanceResponse[Result[Seq[OCOOrderResponse.OCOOrder]]]] =
    allOrderListRequest(fromId, startTime, endTime, limit, recvWindow, timestamp)
      .liftTo[F]
      .map(security.putHeader)
      .map(security.sign)
      .flatMap { uri =>
        client.fetch(Request[F](GET, uri)) { r =>
          BinanceResponse.create[F, Seq[OCOOrder]](r)
        }
      }

  override def openOrderList(recvWindow: Option[Long],
                             timestamp: Instant): F[response.BinanceResponse[Result[Seq[OCOOrderResponse.OCOOrder]]]] =
    openOrderListRequest(recvWindow, timestamp)
      .liftTo[F]
      .map(security.putHeader)
      .map(security.sign)
      .flatMap { uri =>
        client.fetch(Request[F](GET, uri)) { r =>
          BinanceResponse.create[F, Seq[OCOOrder]](r)
        }
      }
}

object LiveOCOClient {
  def make[F[_]: Sync](client: Client[F], securityService: SecurityService): F[OCOClient[F]] =
    Sync[F].delay(
      new LiveOCOClient[F](client, securityService)
    )
}

object OCOClientRequests extends BinanceRestEndpoint {

  import client.clients.rest.client.QueryParams.QueryEncoder._

  def orderRequest(symbol: Symbol,
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
            recvWindow: Option[Long]) =
    Uri
      .fromString(baseEndpoint + api + "/order/oco")
      .map { uri =>
        uri
          .withQueryParam("symbol", symbol.value)
          .withQueryParam("side", side)
          .withQueryParam("quantity", quantity)
          .withQueryParam("price", price)
          .withQueryParam("stopPrice", stopPrice)
          .withQueryParam("timestamp", timestamp.toEpochMilli)
          .withOptionQueryParam("listClientOrderId", listClientOrderId)
          .withOptionQueryParam("limitClientOrderId", listClientOrderId)
          .withOptionQueryParam("limitClientOrderId", limitClientOrderId)
          .withOptionQueryParam("limitIcebergQty", limitIcebergQty)
          .withOptionQueryParam("stopClientOrderId", stopClientOrderId)
          .withOptionQueryParam("stopLimitPrice", stopLimitPrice)
          .withOptionQueryParam("stopIcebergQty", stopIcebergQty)
          .withOptionQueryParam("stopLimitTimeInForce", stopLimitTimeInForce)
          .withOptionQueryParam("recvWindow", recvWindow)
      }

  def cancelOrderRequest(symbol: Symbol,
                      orderListId: Option[OrderListId],
                      listClientOrderId: Option[String],
                      newClientOrderId: Option[OrderId],
                      recvWindow: Option[Long],
                      timestamp: Instant) =
    Uri.fromString(baseEndpoint + api + "/orderList")
      .map {uri =>
        uri
          .withQueryParam("symbol", symbol)
          .withOptionQueryParam("orderListId", orderListId)
          .withOptionQueryParam("listClientOrderId", listClientOrderId)
          .withOptionQueryParam("newClientOrderId", newClientOrderId)
          .withOptionQueryParam("recvWindow", recvWindow)
          .withQueryParam("timestamp", timestamp)
      }

  def orderListRequest(orderListId: Option[OrderListId],
                         origClientOrderId: Option[OrderId],
                         recvWindow: Option[Long],
                         timestamp: Instant) =
    Uri.fromString(baseEndpoint + api + "/orderList")
      .map { uri =>
        uri
          .withOptionQueryParam("orderListId", orderListId)
          .withOptionQueryParam("origClientOrderId", origClientOrderId)
          .withOptionQueryParam("recvWindow", recvWindow)
          .withQueryParam("timestamp", timestamp)
      }

  def allOrderListRequest(fromId: Option[OrderId],
                            startTime: Option[Instant],
                            endTime: Option[Instant],
                            limit: Option[Int],
                            recvWindow: Option[Long],
                            timestamp: Instant) =
    Uri.fromString(baseEndpoint + api + "/allOrderList")
      .map { uri =>
        uri
          .withOptionQueryParam("fromId", fromId)
          .withOptionQueryParam("startTime", startTime)
          .withOptionQueryParam("endTime", endTime)
          .withOptionQueryParam("limit", limit)
          .withOptionQueryParam("recvWindow", recvWindow)
          .withQueryParam("timestamp", timestamp)
      }

  def openOrderListRequest(recvWindow: Option[Long],
                             timestamp: Instant) =
    Uri.fromString(baseEndpoint + api + "/openOrderList")
      .map { uri =>
        uri
          .withOptionQueryParam("recvWindow", recvWindow)
          .withQueryParam("timestamp", timestamp)
      }

}