package client.clients

import cats.effect.{Concurrent, Resource, Timer}
import client.algebra.BinanceConnection
import client.clients.BinanceConnectionLive.buildRequest
import client.domain.{BinanceWSRequest, Event}
import fs2.{RaiseThrowable, Stream}
import io.circe.parser.parse
import io.circe.{Decoder, Error, Json, ParsingFailure}
import org.http4s.Uri
import org.http4s.client.jdkhttpclient.{WSClient, WSFrame, WSRequest}

import scala.concurrent.duration.{DurationInt, FiniteDuration}


class BinanceConnectionLive[F[_]: Concurrent: Timer: RaiseThrowable](client: WSClient[F],
                                                                     pingInterval: FiniteDuration)
  extends BinanceConnection[F] with CirceJson {

  override def connect(requests: Set[BinanceWSRequest]): Resource[F, Stream[F, Either[Error, Event]]] = {
    val request = buildRequest(requests)
    client
      .connectHighLevel(request)
      .map { connection =>
        val pingStream =
          Stream
            .awakeDelay[F](pingInterval)
            .evalTap(_ => connection.sendPing())

        val incomingStream =
          connection
            .receiveStream
            .collect { case f: WSFrame.Text => f.data}
            .map(toArray)
            .flatMap(unbatch[Event])

        incomingStream.concurrently(pingStream)
      }
  }

  private def unbatch[T: Decoder](batch: Either[ParsingFailure, Vector[Json]]): Stream[F, Either[Error, T]] =
    batch match {
      case Left(error) => Stream.emit(Left(error))
      case Right(jsons) => Stream.emits(jsons).map(_.as[T])
    }

  private def toArray(value: String): Either[ParsingFailure, Vector[Json]] = {
    parse(value).map { json =>
      if (json.isArray) json.asArray.get
      else Vector(json)
    }
  }
}


object BinanceConnectionLive extends {
  private val endPoint = Uri.unsafeFromString("wss://stream.binance.com:9443/ws")

  def apply[F[_]: Concurrent: Timer: RaiseThrowable](client: WSClient[F],
                                                     pingInterval: FiniteDuration = 20.second): BinanceConnectionLive[F] = {
    new BinanceConnectionLive(client, pingInterval)
  }

  def buildRequest(requests: Set[BinanceWSRequest]): WSRequest = {
    val streams = requests.map(_.streamName).toList
    val uri = streams.foldLeft(endPoint)((uri, path) => uri / path)
    val request = WSRequest(uri)

    request
  }
}
