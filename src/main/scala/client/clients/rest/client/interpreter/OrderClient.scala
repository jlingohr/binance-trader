package client.clients.rest.client.interpreter

import java.time.Instant

import cats.effect.Sync
import client.algebra.SecurityService
import client.clients.rest.BinanceRestEndpoint
import client.clients.rest.client.{CirceHttpJson, OrderClient}
import client.domain.http.response
import client.domain.http.response.{BinanceResponse, Result}
import client.domain.orders.http.OrderResponse._
import client.domain.orders.http.{OrderOptions, OrderType, Side}
import client.domain.params.OrderId
import client.domain.symbols.Symbol
import client.domain.{params, symbols}
import client.effects.effects.MonadThrow
import org.http4s.{Request, Uri}
import org.http4s.circe.JsonDecoder
import org.http4s.client.Client
import org.http4s.dsl.Http4sDsl
import cats.implicits._

class LiveOrderClient[F[_]: Sync: JsonDecoder: MonadThrow] (client: Client[F], security: SecurityService)
  extends OrderClient[F]
    with Http4sDsl[F]
    with CirceHttpJson{

  import OrderClientRequests._

  override def order(symbol: symbols.Symbol,
                     side: Side,
                     orderType: OrderType,
                     timestamp: Instant,
                     options: OrderOptions): F[response.BinanceResponse[Result[OrderFull]]] =
    orderRequest(symbol, side, orderType, timestamp, options)
      .liftTo[F]
      .map(security.putHeader)
      .map(security.sign)
      .flatMap { uri =>
        client.fetch(Request[F](POST, uri)) { r =>
          BinanceResponse.create[F, OrderFull](r)
        }
      }

  override def test(symbol: symbols.Symbol,
                    side: Side,
                    orderType: OrderType,
                    timestamp: Instant,
                    options: OrderOptions): F[response.BinanceResponse[Result[Unit]]] =
    testRequest(symbol, side, orderType, timestamp, options)
      .liftTo[F]
      .map(security.putHeader)
      .map(security.sign)
      .flatMap { uri =>
        client.fetch(Request[F](POST, uri)) { r =>
          BinanceResponse.create[F, Unit](r)
        }
      }

  override def orderStatus(symbol: symbols.Symbol,
                           orderId: Option[params.OrderId],
                           origClientOrderId: Option[params.OrderId],
                           recvWindow: Option[Long],
                           timestamp: Instant): F[response.BinanceResponse[Result[Order]]] =
    orderStatusRequest(symbol, orderId, origClientOrderId, recvWindow, timestamp)
      .liftTo[F]
      .map(security.putHeader)
      .map(security.sign)
      .flatMap { uri =>
        client.fetch(Request[F](GET, uri)) { r =>
          BinanceResponse.create[F, Order](r)
        }
      }

  override def cancel(symbol: symbols.Symbol,
                      orderId: Option[params.OrderId],
                      origClientOrderId: Option[params.OrderId],
                      newClientOrderId: Option[params.OrderId],
                      recvWindow: Option[Long],
                      timestamp: Instant): F[response.BinanceResponse[Result[CancelOrder]]] =
    cancelRequest(symbol, orderId, origClientOrderId, newClientOrderId, recvWindow, timestamp)
      .liftTo[F]
      .map(security.putHeader)
      .map(security.sign)
      .flatMap { uri =>
        client.fetch(Request[F](DELETE, uri)) { r =>
          BinanceResponse.create[F, CancelOrder](r)
        }
      }

  override def cancelAll(symbol: symbols.Symbol,
                         recvWindow: Option[Long],
                         timestamp: Instant): F[response.BinanceResponse[Result[Seq[CancelOrder]]]] =
    cancelAllRequest(symbol, recvWindow, timestamp)
      .liftTo[F]
      .map(security.putHeader)
      .map(security.sign)
      .flatMap { uri =>
        client.fetch(Request[F](DELETE, uri)) { r =>
          BinanceResponse.create[F, Seq[CancelOrder]](r)
        }
      }

  override def currentOpenOrders(symbol: symbols.Symbol,
                                 recvWindow: Option[Long],
                                 timestamp: Instant): F[response.BinanceResponse[Result[Seq[Order]]]] =
    currentOpenOrdersRequest(symbol, recvWindow, timestamp)
      .liftTo[F]
      .map(security.putHeader)
      .map(security.sign)
      .flatMap { uri =>
        client.fetch(Request[F](GET, uri)) { r =>
          BinanceResponse.create[F, Seq[Order]](r)
        }
      }

  override def allOrders(symbol: symbols.Symbol,
                         orderId: Option[params.OrderId],
                         startTime: Option[Instant],
                         endTime: Option[Instant],
                         limit: Option[Int],
                         recvWindow: Option[Long],
                         timestamp: Instant): F[response.BinanceResponse[Result[Seq[Order]]]] =
    allOrdersRequest(symbol, orderId, startTime, endTime, limit, recvWindow, timestamp)
      .liftTo[F]
      .map(security.putHeader)
      .map(security.sign)
      .flatMap { uri =>
        client.fetch(Request[F](GET, uri)) { r =>
          BinanceResponse.create[F, Seq[Order]](r)
        }
      }
}

object OrderClientRequests extends BinanceRestEndpoint {

  import client.clients.rest.client.QueryParams.QueryEncoder._

  def orderRequest(symbol: symbols.Symbol,
            side: Side,
            orderType: OrderType,
            timestamp: Instant,
            options: OrderOptions) =
    Uri
      .fromString(baseEndpoint + api + "/ticker/order")
      .map { uri =>
        options
          .withUri(uri)
          .withQueryParam("symbol", symbol.value)
          .withQueryParam("side", side)
          .withQueryParam("type", orderType)
          .withQueryParam("timestamp", timestamp.toEpochMilli)
      }

  def testRequest(symbol: symbols.Symbol,
           side: Side,
           orderType: OrderType,
           timestamp: Instant,
           options: OrderOptions) =
    Uri.fromString(baseEndpoint + api + "/orders/test")
      .map { uri =>
        options
          .withUri(uri)
          .withQueryParam("symbol", symbol.value)
          .withQueryParam("side", side)
          .withQueryParam("type", orderType)
          .withQueryParam("timestamp", timestamp.toEpochMilli)
      }

  def orderStatusRequest(symbol: Symbol,
                  orderId: Option[OrderId],
                  origClientOrderId: Option[OrderId],
                  recvWindow: Option[Long],
                  timestamp: Instant) =
    Uri.fromString(baseEndpoint + api + "/order")
      .map { uri =>
        uri
          .withQueryParam("symbol", symbol.value)
          .withOptionQueryParam("orderId", orderId)
          .withOptionQueryParam("origClientOrderId", origClientOrderId)
          .withOptionQueryParam("recvWindow", recvWindow)
          .withQueryParam("timestamp", timestamp.toEpochMilli)
      }

  def cancelRequest(symbol: symbols.Symbol,
             orderId: Option[params.OrderId],
             origClientOrderId: Option[params.OrderId],
             newClientOrderId: Option[params.OrderId],
             recvWindow: Option[Long], timestamp: Instant) =
    Uri.fromString(baseEndpoint + api + "/order")
      .map { uri =>
        uri
          .withQueryParam("symbol", symbol.value)
          .withOptionQueryParam("orderId", orderId)
          .withOptionQueryParam("origClientOrderId", origClientOrderId)
          .withOptionQueryParam("newClientOrderId", newClientOrderId)
          .withOptionQueryParam("recvWindow", recvWindow)
          .withQueryParam("timestamp", timestamp.toEpochMilli)
      }

  def cancelAllRequest(symbol: symbols.Symbol,
                recvWindow: Option[Long],
                timestamp: Instant) =
    Uri.fromString(baseEndpoint + api + "/openOrders")
      .map { uri =>
        uri
          .withQueryParam("symbol", symbol.value)
          .withOptionQueryParam("recvWindow", recvWindow)
          .withQueryParam("timestamp", timestamp.toEpochMilli)
      }

  def currentOpenOrdersRequest(symbol: Symbol,
                        recvWindow: Option[Long],
                        timestamp: Instant) =
    Uri.fromString(baseEndpoint + api + "/openOrders")
      .map { uri =>
        uri
          .withQueryParam("symbol", symbol.value)
          .withOptionQueryParam("recvWindow", recvWindow)
          .withQueryParam("timestamp", timestamp.toEpochMilli)
      }

  def allOrdersRequest(symbol: Symbol,
                orderId: Option[OrderId],
                startTime: Option[Instant],
                endTime: Option[Instant],
                limit: Option[Int],
                recvWindow: Option[Long], timestamp: Instant) =
    Uri.fromString(baseEndpoint + api + "/allOrders")
      .map { uri =>
        uri
          .withQueryParam("symbol", symbol.value)
          .withOptionQueryParam("orderId", orderId)
          .withOptionQueryParam("startTime", startTime.map(_.toEpochMilli))
          .withOptionQueryParam("endTime", endTime.map(_.toEpochMilli))
          .withOptionQueryParam("limit", limit)
          .withOptionQueryParam("recvWindow", recvWindow)
          .withQueryParam("timestamp", timestamp.toEpochMilli)
      }

}
