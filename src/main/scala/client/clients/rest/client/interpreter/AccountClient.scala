package client.clients.rest.client.interpreter

import java.time.Instant

import cats.effect.Sync
import cats.implicits._
import client.algebra.SecurityService
import client.clients.rest.BinanceRestEndpoint
import client.clients.rest.client.{AccountClient, CirceHttpJson}
import client.domain.account.http.AccountResponse.AccountInformation
import client.domain.http.response.{BinanceResponse, Result}
import client.domain.trades.http.trades.Trade
import client.domain.{params, symbols}
import client.effects.effects.MonadThrow
import org.http4s.circe.JsonDecoder
import org.http4s.client.Client
import org.http4s.dsl.Http4sDsl
import org.http4s.{Request, Uri}


class LiveAccountClient[F[_]: Sync: JsonDecoder: MonadThrow](client: Client[F], security: SecurityService)
  extends AccountClient[F]
    with Http4sDsl[F]
    with CirceHttpJson {

  import AccountClientRequests._

  override def account(recvWindow: Option[Long],
                       timestamp: Instant): F[BinanceResponse[Result[AccountInformation]]] =
    accountRequest(recvWindow, timestamp)
      .map(security.putHeader)
      .map(security.sign)
      .liftTo[F]
      .flatMap { uri =>
        client.fetch(Request[F](GET, uri)) { r =>
          BinanceResponse.create[F, AccountInformation](r)
        }
      }

  override def myTrades(symbol: symbols.Symbol,
                        startTime: Option[Instant],
                        endTime: Option[Instant],
                        fromId: Option[params.TradeId],
                        limit: Option[Int],
                        recvWindow: Option[Long],
                        timestamp: Instant): F[BinanceResponse[Result[Seq[Trade]]]] =
    myTradesRequest(symbol, startTime, endTime, fromId, limit, recvWindow, timestamp)
      .map(security.putHeader)
      .map(security.sign)
      .liftTo[F]
      .flatMap { uri =>
        client.fetch(Request[F](GET, uri)) { r =>
          BinanceResponse.create[F, Seq[Trade]](r)
        }
      }
}

object AccountClientRequests extends BinanceRestEndpoint {

  import client.clients.rest.client.QueryParams.QueryEncoder._

  def accountRequest(recvWindow: Option[Long],
              timestamp: Instant) =
    Uri.fromString(baseEndpoint + api + "/account")
      .map { uri =>
        uri
          .withOptionQueryParam("recvWindow", recvWindow)
          .withQueryParam("timestamp", timestamp.toEpochMilli)
      }


  def myTradesRequest(symbol: symbols.Symbol,
                        startTime: Option[Instant],
                        endTime: Option[Instant],
                        fromId: Option[params.TradeId],
                        limit: Option[Int],
                        recvWindow: Option[Long],
                        timestamp: Instant) =
    Uri.fromString(baseEndpoint + api + "/myTrades")
      .map { uri =>
        uri
          .withQueryParam("symbol", symbol)
          .withOptionQueryParam("startTime", startTime.map(_.toEpochMilli))
          .withOptionQueryParam("endTime", endTime.map(_.toEpochMilli))
          .withOptionQueryParam("fromId", fromId)
          .withOptionQueryParam("limit", limit)
          .withOptionQueryParam("recvWindow", recvWindow)
          .withQueryParam("timestamp", timestamp)
      }

}
