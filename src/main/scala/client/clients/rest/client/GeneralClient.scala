package client.clients.rest.client

import client.domain.http.BinanceResponse

trait GeneralClient[F[_]] {

  def ping: F[BinanceResponse]

  def time: F[BinanceResponse]

  def exchangeInfo: F[BinanceResponse]

}
