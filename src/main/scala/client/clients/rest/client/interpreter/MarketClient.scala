package client.clients.rest.client.interpreter

import java.time.Instant

import client.clients.rest.client.MarketClient
import client.domain.depths.http.DepthLimit
import client.domain.depths.depths._
import client.domain.depths.http.DepthLimit._
import client.domain.trades.http.trades._
import client.domain.http.BinanceResponse
import client.domain.klines.KlineInterval
import client.domain.klines.http.klines._
import client.domain.{params, symbols}
import client.domain.AveragePrice
import client.clients.rest.client.CirceHttpJson
import client.effects.effects.MonadThrow
import org.http4s.circe.JsonDecoder
import org.http4s.client.Client
import org.http4s.dsl.Http4sDsl
import org.http4s._
import cats.implicits._
import org.http4s.circe._

import scala.util.control.NoStackTrace

case class BinanceResponseError(cause: String) extends NoStackTrace

class LiveMarketClient[F[_]: JsonDecoder: MonadThrow](client: Client[F])
  extends MarketClient[F] with Http4sDsl[F] with CirceHttpJson {

  override def depth(symbol: symbols.Symbol,
                     limit: DepthLimit = Depth100): F[PartialDepthUpdate] =
    Uri
      .fromString(baseEndpoint + api + "/depth")
      .liftTo[F]
      .map { uri =>
        uri
          .withQueryParam("symbol", symbol.value)
          .withQueryParam("limit", limit.value)
      }
      .flatMap { uri =>
        client.get[PartialDepthUpdate](uri) { r =>
          if (r.status == Status.Ok || r.status == Status.Conflict) {
            r.asJsonDecode[PartialDepthUpdate]
          } else {
            BinanceResponseError(
              Option(r.status.reason).getOrElse("Unknown")
            ).raiseError[F, PartialDepthUpdate]
          }
        }
      }

  override def trades(symbol: symbols.Symbol,
                      limit: Option[Int]): F[Seq[Trade]] =
    Uri.fromString(baseEndpoint + api + "/trades")
      .liftTo[F]
      .map { uri =>
        uri
          .withQueryParam("symbol", symbol.value)
          .withOptionQueryParam("limit", limit)
      }
      .flatMap { uri =>
        client.get[Seq[Trade]](uri) { r =>
          if (r.status == Status.Ok || r.status == Status.Conflict) {
            r.asJsonDecode[Seq[Trade]]
          } else {
            BinanceResponseError(
              Option(r.status.reason).getOrElse("Unknown")
            ).raiseError[F, Seq[Trade]]
          }
        }
      }

  override def historicalTrades(symbol: symbols.Symbol,
                                limit: Option[Int],
                                fromId: Option[params.TradeId]): F[BinanceResponse] = ???

  override def aggTrades(symbol: symbols.Symbol,
                         fromId: Option[params.TradeId],
                         startTime: Option[Instant],
                         endTime: Option[Instant],
                         limit: Option[Int]): F[Seq[AggTrade]] =
    Uri.fromString(baseEndpoint + api + "/aggTrades")
      .liftTo[F]
      .map { uri =>
        uri
          .withQueryParam("symbol", symbol.value)
          .withOptionQueryParam("fromId", fromId.map(_.value))
          .withOptionQueryParam("startTime", startTime.map(_.toEpochMilli))
          .withOptionQueryParam("endTime", endTime.map(_.toEpochMilli))
          .withOptionQueryParam("limit", limit)
      }
      .flatMap { uri =>
        client.get[Seq[AggTrade]](uri) { r =>
          if (r.status == Status.Ok || r.status == Status.Conflict) {
            r.asJsonDecode[Seq[AggTrade]]
          } else {
            BinanceResponseError(
              Option(r.status.reason).getOrElse("Unknown")
            ).raiseError[F, Seq[AggTrade]]
          }
        }
      }

  override def klines(symbol: symbols.Symbol,
                      interval: KlineInterval,
                      startTime: Option[Instant],
                      endTime: Option[Instant],
                      limit: Option[Int]): F[Seq[Kline]] =
    Uri.fromString(baseEndpoint + api + "/klines")
      .liftTo[F]
      .map { uri =>
        uri
          .withQueryParam("symbol", symbol.value)
          .withQueryParam("interval", interval)
          .withOptionQueryParam("startTime", startTime.map(_.toEpochMilli))
          .withOptionQueryParam("endTime", endTime.map(_.toEpochMilli))
          .withOptionQueryParam("limit", limit)
      }
      .flatMap { uri =>
        client.get[Seq[Kline]](uri) { r =>
          if (r.status == Status.Ok || r.status == Status.Conflict) {
            r.asJsonDecode[Seq[Kline]]
          } else {
            BinanceResponseError(
              Option(r.status.reason).getOrElse("Unknown")
            ).raiseError[F, Seq[Kline]]
          }
        }
      }

  override def avgPrice(symbol: symbols.Symbol): F[AveragePrice] =
    Uri.fromString(baseEndpoint + api + "/avgPrice")
      .liftTo[F]
      .flatMap { uri =>
        client.get[AveragePrice](uri) { r =>
          if (r.status == Status.Ok || r.status == Status.Conflict) {
            r.asJsonDecode[AveragePrice]
          } else {
            BinanceResponseError(
              Option(r.status.reason).getOrElse("Unknown")
            ).raiseError[F, AveragePrice]
          }
        }
      }
}
