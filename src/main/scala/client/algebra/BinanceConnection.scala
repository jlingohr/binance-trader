package client.algebra

import cats.effect.Resource
import client.domain.{BinanceWSRequest, Event}
import fs2.Stream
import io.circe.{Error}


trait BinanceConnection[F[_]] {
  def connect(requests: Set[BinanceWSRequest]): Resource[F, Stream[F, Either[Error, Event]]]
}

