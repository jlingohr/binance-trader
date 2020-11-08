package client.clients.rest.client.interpreter

import java.time.Instant

import cats.effect.Sync
import client.clients.rest.BinanceRestEndpoint
import client.clients.rest.client.{CirceHttpJson, MarketClient}
import client.domain.depths.depths._
import client.domain.depths.http.DepthLimit
import client.domain.depths.http.DepthLimit.Depth100
import client.domain.http.response
import client.domain.http.response.{BinanceResponse, Result}
import client.domain.klines.KlineInterval
import client.domain.klines.http.klines._
import client.domain.trades.http.trades._
import client.domain.{AveragePrice, params, symbols}
import org.http4s.Uri
import org.http4s.circe.JsonDecoder
import org.http4s.client.Client
import org.http4s.dsl.Http4sDsl
import client.effects.effects.MonadThrow
import cats.implicits._

class LiveMarketClient[F[_]: Sync: JsonDecoder: MonadThrow](client: Client[F])
  extends MarketClient[F]
    with Http4sDsl[F]
    with CirceHttpJson {

  import MarketClientRequests._

  override def depth(symbol: symbols.Symbol,
                     limit: DepthLimit): F[response.BinanceResponse[Result[PartialDepthUpdate]]] = {
    depthRequest(symbol, limit)
      .liftTo[F]
      .flatMap { uri =>
        client.get(uri) { r =>
          BinanceResponse.create[F, PartialDepthUpdate](r)
        }
      }

  }

  override def trades(symbol: symbols.Symbol,
                      limit: Option[Int]): F[response.BinanceResponse[Result[Seq[Trade]]]] =
    tradesRequest(symbol, limit)
      .liftTo[F]
      .flatMap { uri =>
        client.get(uri) { r =>
          BinanceResponse.create[F, Seq[Trade]](r)
        }
      }

  override def historicalTrades(symbol: symbols.Symbol,
                                limit: Option[Int],
                                fromId: Option[params.TradeId]): F[response.BinanceResponse[Result[Trade]]] = ???

  override def aggTrades(symbol: symbols.Symbol,
                         fromId: Option[params.TradeId],
                         startTime: Option[Instant],
                         endTime: Option[Instant],
                         limit: Option[Int]): F[response.BinanceResponse[Result[Seq[AggTrade]]]] =
    aggTradesRequest(symbol, fromId, startTime, endTime, limit)
      .liftTo[F]
      .flatMap { uri =>
        client.get(uri) { r =>
          BinanceResponse.create[F, Seq[AggTrade]](r)
        }
      }

  override def klines(symbol: symbols.Symbol,
                      interval: KlineInterval,
                      startTime: Option[Instant],
                      endTime: Option[Instant],
                      limit: Int): F[response.BinanceResponse[Result[Seq[Kline]]]] =
    klinesRequest(symbol, interval, startTime, endTime, limit)
      .liftTo[F]
      .flatMap { uri =>
        client.get(uri) { r =>
          BinanceResponse.create[F, Seq[Kline]](r)
        }
      }

  override def avgPrice(symbol: symbols.Symbol): F[response.BinanceResponse[Result[AveragePrice]]] =
    avgPriceRequest(symbol)
      .liftTo[F]
      .flatMap {uri =>
        client.get(uri) { r =>
          BinanceResponse.create[F, AveragePrice](r)
        }
      }
}

object LiveMarketClient {
  def make[F[_]: Sync](client: Client[F]): F[MarketClient[F]] =
    Sync[F].delay(
      new LiveMarketClient[F](client)
    )
}

object MarketClientRequests extends BinanceRestEndpoint {

  def depthRequest(symbol: symbols.Symbol,
            limit: DepthLimit = Depth100) =
    Uri
      .fromString(baseEndpoint + api + "/depth")
      .map { uri =>
        uri
          .withQueryParam("symbol", symbol.value)
          .withQueryParam("limit", limit.value)
      }

  def tradesRequest(symbol: symbols.Symbol,
             limit: Option[Int]) =
    Uri.fromString(baseEndpoint + api + "/trades")
      .map { uri =>
        uri
          .withQueryParam("symbol", symbol.value)
          .withOptionQueryParam("limit", limit)
      }

  def historicalTradesRequest(symbol: symbols.Symbol,
                       limit: Option[Int],
                       fromId: Option[params.TradeId]) = ???

  def aggTradesRequest(symbol: symbols.Symbol,
                fromId: Option[params.TradeId],
                startTime: Option[Instant],
                endTime: Option[Instant],
                limit: Option[Int]) =
    Uri.fromString(baseEndpoint + api + "/aggTrades")
      .map { uri =>
        uri
          .withQueryParam("symbol", symbol.value)
          .withOptionQueryParam("fromId", fromId.map(_.value))
          .withOptionQueryParam("startTime", startTime.map(_.toEpochMilli))
          .withOptionQueryParam("endTime", endTime.map(_.toEpochMilli))
          .withOptionQueryParam("limit", limit)
      }

  def klinesRequest(symbol: symbols.Symbol,
             interval: KlineInterval,
             startTime: Option[Instant],
             endTime: Option[Instant],
             limit: Int = 500) =
    Uri.fromString(baseEndpoint + api + "/klines")
      .map { uri =>
        uri
          .withQueryParam("symbol", symbol.value)
          .withQueryParam("interval", interval.value)
          .withOptionQueryParam("startTime", startTime.map(_.toEpochMilli))
          .withOptionQueryParam("endTime", endTime.map(_.toEpochMilli))
          .withQueryParam("limit", limit)
      }

  def avgPriceRequest(symbol: symbols.Symbol) =
    Uri.fromString(baseEndpoint + api + "/avgPrice")
      .map { uri =>
        uri
          .withQueryParam("symbol", symbol.value)
      }
}