package client.clients.rest.client.interpreter

import cats.implicits._
import client.clients.rest.BinanceRest
import client.clients.rest.client.{CirceHttpJson, TickerClient}
import client.domain.symbols
import client.domain.tickers.http.tickers._
import client.effects.effects.MonadThrow
import org.http4s.circe.{JsonDecoder, _}
import org.http4s.client.Client
import org.http4s.dsl.Http4sDsl
import org.http4s.{Status, Uri}

class LiveTickerClient[F[_]: JsonDecoder: MonadThrow](client: Client[F])
  extends TickerClient[F]
    with Http4sDsl[F]
    with BinanceRest[F]
    with CirceHttpJson {

  override def ticker24(symbol: symbols.Symbol): F[Ticker24Hr] =
    Uri
      .fromString(baseEndpoint + api + "/ticker/24hr")
      .liftTo[F]
      .map(_.withQueryParam("symbol", symbol.value))
      .flatMap { uri =>
        client.get[Ticker24Hr](uri) { r =>
          if (r.status == Status.Ok || r.status == Status.Conflict) {
            r.asJsonDecode[Ticker24Hr]
          } else {
            BinanceResponseError(
              Option(r.status.reason).getOrElse("Unknown")
            ).raiseError[F, Ticker24Hr]
          }
        }
      }

  override def price(symbol: symbols.Symbol): F[TickerPrice] =
    Uri
      .fromString(baseEndpoint + api + "/ticker/price")
      .liftTo[F]
      .map(_.withQueryParam("symbol", symbol.value))
      .flatMap { uri =>
        client.get[TickerPrice](uri) { r =>
          if (r.status == Status.Ok || r.status == Status.Conflict) {
            r.asJsonDecode[TickerPrice]
          } else {
            BinanceResponseError(
              Option(r.status.reason).getOrElse("Unknown")
            ).raiseError[F, TickerPrice]
          }
        }
      }

  override def bookTicker(symbol: symbols.Symbol): F[BookTicker] =
    Uri
      .fromString(baseEndpoint + api + "/ticker/bookTicker")
      .liftTo[F]
      .map(_.withQueryParam("symbol", symbol.value))
      .flatMap { uri =>
        client.get[BookTicker](uri) { r =>
          if (r.status == Status.Ok || r.status == Status.Conflict) {
            r.asJsonDecode[BookTicker]
          } else {
            BinanceResponseError(
              Option(r.status.reason).getOrElse("Unknown")
            ).raiseError[F, BookTicker]
          }
        }
      }
}
