package client.clients.rest.client

import client.domain.http.BinanceResponse
import client.domain.symbols.Symbol

trait TickerClient[F[_]] {

  def ticker24(symbol: Option[Symbol]): F[BinanceResponse]

  def price(symbol: Option[Symbol]): F[BinanceResponse]

  def bookTicker(symbol: Option[Symbol]): F[BinanceResponse]

}
