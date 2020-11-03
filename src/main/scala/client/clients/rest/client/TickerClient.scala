package client.clients.rest.client

import client.domain.http.BinanceResponse
import client.domain.symbols.Symbol
import client.domain.tickers.http.tickers.{BookTicker, Ticker24Hr, TickerPrice}

trait TickerClient[F[_]] {

  def ticker24(symbol: Symbol): F[Ticker24Hr]

  def price(symbol: Symbol): F[TickerPrice]

  def bookTicker(symbol: Symbol): F[BookTicker]

}
