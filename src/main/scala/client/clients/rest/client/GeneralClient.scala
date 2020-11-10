package client.clients.rest.client

import client.domain.exchange.http.ExchangeInfo.Info
import client.domain.http.ServerTime
import client.domain.http.response.{BinanceResponse, Result}

trait GeneralClient[F[_]] {

  def ping: F[BinanceResponse[Result[Unit]]]

  def time: F[BinanceResponse[Result[ServerTime]]]

  def exchangeInfo: F[BinanceResponse[Result[Info]]]

}
