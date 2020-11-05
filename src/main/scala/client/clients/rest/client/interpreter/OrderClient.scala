package client.clients.rest.client.interpreter

import java.time.Instant

import cats.implicits._
import client.algebra.SecurityService
import client.clients.rest.BinanceRest
import client.clients.rest.client.QueryParams.QueryEncoder._
import client.clients.rest.client.{CirceHttpJson, OrderClient}
import client.domain.orders.http.OrderResponse.{CancelOrder, Order, OrderFull}
import client.domain.orders.http.{OrderOptions, OrderType, Side}
import client.domain.params.OrderId
import client.domain.symbols.Symbol
import client.domain.{params, symbols}
import client.effects.effects.MonadThrow
import org.http4s.circe.{JsonDecoder, _}
import org.http4s.client.Client
import org.http4s.dsl.Http4sDsl
import org.http4s.{Request, Status, Uri}


class LiveOrderClient[F[_]: JsonDecoder: MonadThrow](client: Client[F], security: SecurityService[F])
  extends OrderClient[F]
    with Http4sDsl[F]
    with BinanceRest[F]
    with CirceHttpJson {

  override def order(symbol: symbols.Symbol,
                     side: Side,
                     orderType: OrderType,
                     timestamp: Instant,
                     options: OrderOptions): F[OrderFull] = {
    Uri
      .fromString(baseEndpoint + api + "/ticker/order")
      .liftTo[F]
      .map { uri =>
        options
          .withUri(uri)
          .withQueryParam("symbol", symbol.value)
          .withQueryParam("side", side)
          .withQueryParam("type", orderType)
          .withQueryParam("timestamp", timestamp.toEpochMilli)
      }
      .map(security.putHeader)
      .map(security.sign)
      .flatMap { uri =>
        client.fetch[OrderFull](Request[F](POST, uri)) { r =>
          if (r.status == Status.Ok || r.status == Status.Conflict) {
            r.asJsonDecode[OrderFull]
          } else {
            BinanceResponseError(
              Option(r.status.reason).getOrElse("Unknown")
            ).raiseError[F, OrderFull]
          }
        }
      }
  }

  override def test(symbol: symbols.Symbol,
                    side: Side,
                    orderType: OrderType,
                    timestamp: Instant,
                    options: OrderOptions): F[Unit] =
    Uri.fromString(baseEndpoint + api + "/orders/test")
      .liftTo[F]
      .map { uri =>
        options
          .withUri(uri)
          .withQueryParam("symbol", symbol.value)
          .withQueryParam("side", side)
          .withQueryParam("type", orderType)
          .withQueryParam("timestamp", timestamp.toEpochMilli)
      }
      .map(security.putHeader)
      .map(security.sign)
      .flatMap { uri =>
        client.fetch[Unit](Request[F](POST, uri)) { r =>
          if (r.status == Status.Ok || r.status == Status.Conflict) {
            r.asJsonDecode[Unit]
          } else {
            BinanceResponseError(
              Option(r.status.reason).getOrElse("Unknown")
            ).raiseError[F, Unit]
          }
        }
      }

  override def orderStatus(symbol: Symbol,
                  orderId: Option[OrderId],
                  origClientOrderId: Option[OrderId],
                  recvWindow: Option[Long],
                  timestamp: Instant): F[Order] =
    Uri.fromString(baseEndpoint + api + "/order")
      .liftTo[F]
      .map { uri =>
        uri
          .withQueryParam("symbol", symbol.value)
          .withOptionQueryParam("orderId", orderId)
          .withOptionQueryParam("origClientOrderId", origClientOrderId)
          .withOptionQueryParam("recvWindow", recvWindow)
          .withQueryParam("timestamp", timestamp.toEpochMilli)
      }
      .map(security.putHeader)
      .map(security.sign)
      .flatMap { uri =>
        client.fetch[Order](Request[F](GET, uri)) { r =>
          if (r.status == Status.Ok || r.status == Status.Conflict) {
            r.asJsonDecode[Order]
          } else {
            BinanceResponseError(
              Option(r.status.reason).getOrElse("Unknown")
            ).raiseError[F, Order]
          }
        }
      }

  override def cancel(symbol: symbols.Symbol,
                      orderId: Option[params.OrderId],
                      origClientOrderId: Option[params.OrderId],
                      newClientOrderId: Option[params.OrderId],
                      recvWindow: Option[Long], timestamp: Instant): F[CancelOrder] =
    Uri.fromString(baseEndpoint + api + "/order")
      .liftTo[F]
      .map { uri =>
        uri
          .withQueryParam("symbol", symbol.value)
          .withOptionQueryParam("orderId", orderId)
          .withOptionQueryParam("origClientOrderId", origClientOrderId)
          .withOptionQueryParam("newClientOrderId", newClientOrderId)
          .withOptionQueryParam("recvWindow", recvWindow)
          .withQueryParam("timestamp", timestamp.toEpochMilli)
      }
      .map(security.putHeader)
      .map(security.sign)
      .flatMap { uri =>
        client.fetch[CancelOrder](Request[F](DELETE, uri)) { r =>
          if (r.status == Status.Ok || r.status == Status.Conflict) {
            r.asJsonDecode[CancelOrder]
          } else {
            BinanceResponseError(
              Option(r.status.reason).getOrElse("Unknown")
            ).raiseError[F, CancelOrder]
          }
        }
      }

  override def cancelAll(symbol: symbols.Symbol,
                         recvWindow: Option[Long],
                         timestamp: Instant): F[Seq[CancelOrder]] =
    Uri.fromString(baseEndpoint + api + "/openOrders")
      .liftTo[F]
      .map { uri =>
        uri
          .withQueryParam("symbol", symbol.value)
          .withOptionQueryParam("recvWindow", recvWindow)
          .withQueryParam("timestamp", timestamp.toEpochMilli)
      }
      .map(security.putHeader)
      .map(security.sign)
      .flatMap { uri =>
        client.fetch[Seq[CancelOrder]](Request[F](DELETE, uri)) { r =>
          if (r.status == Status.Ok || r.status == Status.Conflict) {
            r.asJsonDecode[Seq[CancelOrder]]
          } else {
            BinanceResponseError(
              Option(r.status.reason).getOrElse("Unknown")
            ).raiseError[F, Seq[CancelOrder]]
          }
        }
      }

  override def currentOpenOrders(symbol: Symbol,
                                 recvWindow: Option[Long],
                                 timestamp: Instant): F[Seq[Order]] =
    Uri.fromString(baseEndpoint + api + "/openOrders")
      .liftTo[F]
      .map { uri =>
        uri
          .withQueryParam("symbol", symbol.value)
          .withOptionQueryParam("recvWindow", recvWindow)
          .withQueryParam("timestamp", timestamp.toEpochMilli)
      }
      .map(security.putHeader)
      .map(security.sign)
      .flatMap { uri =>
        client.fetch[Seq[Order]](Request[F](GET, uri)) { r =>
          if (r.status == Status.Ok || r.status == Status.Conflict) {
            r.asJsonDecode[Seq[Order]]
          } else {
            BinanceResponseError(
              Option(r.status.reason).getOrElse("Unknown")
            ).raiseError[F, Seq[Order]]
          }
        }
      }

  override def allOrders(symbol: Symbol,
                         orderId: Option[OrderId],
                         startTime: Option[Instant],
                         endTime: Option[Instant],
                         limit: Option[Int],
                         recvWindow: Option[Long], timestamp: Instant): F[Seq[Order]] =
    Uri.fromString(baseEndpoint + api + "/allOrders")
      .liftTo[F]
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
      .map(security.putHeader)
      .map(security.sign)
      .flatMap { uri =>
        client.fetch[Seq[Order]](Request[F](GET, uri)) { r =>
          if (r.status == Status.Ok || r.status == Status.Conflict) {
            r.asJsonDecode[Seq[Order]]
          } else {
            BinanceResponseError(
              Option(r.status.reason).getOrElse("Unknown")
            ).raiseError[F, Seq[Order]]
          }
        }
      }

}
