package client.domain.exchange.http

import java.time.Instant

import client.domain.http.RateLimiter


object ExchangeInfo {

  case class ExchangeFilter()
  case class ExchangeSymbol()

  case class Info(timezone: String,
                  serverTime: Instant,
                  rateLimits: Seq[RateLimiter],
                  exchangeFilters: Seq[ExchangeFilter],
                  symbols: Seq[ExchangeSymbol])

}
