package client.clients.rest.client.interpreter

import cats.effect.Sync
import cats.implicits._
import client.clients.rest.BinanceRestEndpoint
import client.clients.rest.client.{CirceHttpJson, TickerClient}
import client.domain.http.response
import client.domain.http.response.{BinanceResponse, Result}
import client.domain.symbols
import client.domain.tickers.http.tickers._
import client.effects.effects.MonadThrow
import org.http4s.Uri
import org.http4s.circe.JsonDecoder
import org.http4s.client.Client
import org.http4s.dsl.Http4sDsl

class LiveTickerClient[F[_]: Sync: JsonDecoder: MonadThrow](client: Client[F])
  extends TickerClient[F]
  with Http4sDsl[F]
  with CirceHttpJson {

  import TickerClientRequests._

  override def ticker24(symbol: symbols.Symbol): F[response.BinanceResponse[Result[Ticker24Hr]]] =
    ticker24Request(symbol)
      .liftTo[F]
      .flatMap { uri =>
        client.get(uri) { r =>
          BinanceResponse.create[F, Ticker24Hr](r)
        }
      }

  override def price(symbol: symbols.Symbol): F[response.BinanceResponse[Result[TickerPrice]]] =
    priceRequest(symbol)
      .liftTo[F]
      .flatMap { uri =>
        client.get(uri) { r =>
          BinanceResponse.create[F, TickerPrice](r)
        }
      }

  override def bookTicker(symbol: symbols.Symbol): F[response.BinanceResponse[Result[BookTicker]]] =
    bookTickerRequest(symbol)
      .liftTo[F]
      .flatMap { uri =>
        client.get(uri) { r =>
          BinanceResponse.create[F, BookTicker](r)
        }
      }
}

object LiveTickerClient {
  def make[F[_]: Sync](client: Client[F]): F[TickerClient[F]] =
    Sync[F].delay(
      new LiveTickerClient[F](client)
    )
}

object TickerClientRequests extends BinanceRestEndpoint{

  def ticker24Request(symbol: symbols.Symbol) =
    Uri
      .fromString(baseEndpoint + api + "/ticker/24hr")
      .map(_.withQueryParam("symbol", symbol.value))

  def priceRequest(symbol: symbols.Symbol) =
    Uri
      .fromString(baseEndpoint + api + "/ticker/price")
      .map(_.withQueryParam("symbol", symbol.value))

  def bookTickerRequest(symbol: symbols.Symbol) =
    Uri
      .fromString(baseEndpoint + api + "/ticker/bookTicker")
      .map(_.withQueryParam("symbol", symbol.value))
}

