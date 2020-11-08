package client.clients.rest.client.interpreter

import cats.effect.Sync
import client.clients.rest.BinanceRestEndpoint
import client.clients.rest.client.{CirceHttpJson, GeneralClient}
import client.domain.exchange.http.ExchangeInfo
import client.domain.http.{ServerTime, response}
import client.domain.http.response.{BinanceResponse, Result}
import org.http4s.Uri
import org.http4s.circe.JsonDecoder
import org.http4s.client.Client
import org.http4s.dsl.Http4sDsl
import client.effects.effects.MonadThrow
import cats.implicits._

class LiveGeneralClient[F[_]: Sync: JsonDecoder: MonadThrow](client: Client[F])
  extends GeneralClient[F]
    with Http4sDsl[F]
    with CirceHttpJson {

  import GeneralClientRequests._

  override def ping: F[response.BinanceResponse[Result[Unit]]] =
    pingRequest
      .liftTo[F]
      .flatMap { uri =>
        client.get(uri) { r =>
          BinanceResponse.create[F, Unit](r)
        }
      }

  override def time: F[response.BinanceResponse[Result[ServerTime]]] =
    timeRequest
      .liftTo[F]
      .flatMap { uri =>
        client.get(uri) { r =>
          BinanceResponse.create[F, ServerTime](r)
        }
      }

  override def exchangeInfo: F[response.BinanceResponse[Result[ExchangeInfo.Info]]] =
    exchangeInfoRequest
      .liftTo[F]
      .flatMap { uri =>
        client.get(uri) { r =>
          BinanceResponse.create[F, ExchangeInfo.Info](r)
        }
      }
}

object LiveGeneralClient {
  def make[F[_]: Sync](client: Client[F]): F[GeneralClient[F]] =
    Sync[F].delay(
      new LiveGeneralClient[F](client)
    )
}

object GeneralClientRequests extends BinanceRestEndpoint {

  def pingRequest =
    Uri.fromString(baseEndpoint + api + "/ping")

  def timeRequest =
    Uri.fromString(baseEndpoint + api + "/time")

  def exchangeInfoRequest =
    Uri.fromString(baseEndpoint + api + "/exchangeInfo")

}