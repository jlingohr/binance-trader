package client.domain.http

import cats.Show
import cats.effect.Sync
import client.domain.http.response.BinanceError.{ApiError, DecodeError}
import io.circe.Decoder
import org.http4s.{DecodeFailure, Headers, Response, Status}
import org.http4s.circe.CirceEntityDecoder._
import cats.implicits._
import org.http4s.util.CaseInsensitiveString


object response {

  case class BinanceResponseError(code: Int, message: String, description: Option[String])

  object BinanceResponseError {
    implicit val show: Show[BinanceResponseError] = (t: BinanceResponseError) =>
      s"${t.code} - ${t.message}: ${t.description.getOrElse("")}"
  }

  sealed abstract class BinanceError(message: String, cause: Throwable = None.orNull) extends Exception(message, cause)

  object BinanceError {
    case class DecodeError(failure: DecodeFailure) extends BinanceError(failure.message, failure)
    case class ApiError(response: BinanceResponseError) extends BinanceError(BinanceResponseError.show.show(response))
  }

  case class BinanceResponse[T](status: Status, body: T, headers: Headers) {
    def usedWeight: Int = headers.get(CaseInsensitiveString("x-mbx-used-weight")).map(_.value.toInt).getOrElse(0)
    def orderCount: Int = headers.get(CaseInsensitiveString("x-mbx-order-count")).map(_.value.toInt).getOrElse(0)
  }

  type Result[T] = Either[BinanceError, T]

  object BinanceResponse {
    def apply[F[_], T](response: Response[F], body: T): BinanceResponse[T] =
      BinanceResponse(response.status, body, response.headers)

    def create[F[_]: Sync, T: Decoder](response: Response[F]): F[BinanceResponse[Either[BinanceError, T]]] =
      response match {
        case Status.Successful(success) =>
          success
            .attemptAs[T]
            .leftMap[BinanceError](DecodeError)
            .fold(e => BinanceResponse(success, Left(e)), b => BinanceResponse(success, Right(b)))
        case error =>
          error
          .attemptAs[BinanceResponseError]
          .map[BinanceError](ApiError)
          .leftMap[BinanceError](DecodeError)
          .merge
          .map(e => BinanceResponse(error, Left(e)))
      }
  }

}
