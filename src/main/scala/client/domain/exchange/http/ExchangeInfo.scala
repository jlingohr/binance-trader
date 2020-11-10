package client.domain.exchange.http

import java.time.Instant

import client.domain.exchange.http.filters.{ExchangeFilter, SymbolFilter}
import client.domain.http.{RateLimiter, SymbolStatus}
import client.domain.orders.http.OrderType
import client.domain.params.{Asset, ExchangePermission}
import client.domain.symbols


object ExchangeInfo {

  case class ExchangeSymbol(symbol: symbols.Symbol,
                            status: SymbolStatus,
                            baseAsset: Asset,
                            baseAssetPrecision: Int,
                            quoteAsset: Asset,
                            quotePrecision: Int,
                            baseCommissionPrecision: Int,
                            quoteCommissionPrecision: Int,
                            orderTypes: Seq[OrderType],
                            icebergAllowed: Boolean,
                            ocoAllowed: Boolean,
                            quoteOrderQtyMarketAllowed: Boolean,
                            isSpotTradingAllowed: Boolean,
                            isMarginTradingAllowed: Boolean,
                            filters: Seq[SymbolFilter],
                            permissions: Seq[ExchangePermission])

  case class Info(timezone: String,
                  serverTime: Instant,
                  rateLimits: Seq[RateLimiter],
                  exchangeFilters: Seq[ExchangeFilter],
                  symbols: Seq[ExchangeSymbol])

}
