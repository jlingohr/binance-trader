package client.clients.rest.client

import client.domain.http.response.{BinanceResponse, Result}
import client.domain.symbols.Symbol
import client.domain.tickers.http.tickers.{BookTicker, Ticker24Hr, TickerPrice}

trait TickerClient[F[_]] {

  def ticker24(symbol: Symbol): F[BinanceResponse[Result[Ticker24Hr]]]

  def price(symbol: Symbol): F[BinanceResponse[Result[TickerPrice]]]

  def bookTicker(symbol: Symbol): F[BinanceResponse[Result[BookTicker]]]

}
