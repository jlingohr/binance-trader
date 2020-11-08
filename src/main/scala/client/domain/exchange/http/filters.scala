package client.domain.exchange.http

import client.domain.params.{Price, Quantity}

object filters {

  sealed trait SymbolFilter

  object SymbolFilter {
    case class PriceFilter(symbolFilter: SymbolFilter,
                            minPrice: Price,
                           maxPrice: Price,
                           tickSize: BigDecimal) extends SymbolFilter

    case class PercentPrice(symbolFilter: SymbolFilter,
                            multiplierUp: BigDecimal,
                            multiplierDown: BigDecimal,
                            avgPriceMins: Long) extends SymbolFilter

    case class LotSize(symbolFilter: SymbolFilter,
                       minQty: Quantity,
                       maxQty: Quantity,
                       stepSize: BigDecimal) extends SymbolFilter

    case class MinNotional(symbolFilter: SymbolFilter,
                           minNotional: BigDecimal,
                           applyToMarket: Boolean,
                           avgPriceMins: Long) extends SymbolFilter

    case class IcebergParts(symbolFilter: SymbolFilter,
                            limit: Int) extends SymbolFilter

    case class MarketLotSize(symbolFilter: SymbolFilter,
                             minQty: Quantity,
                             maxQty: Quantity,
                             stepSize: BigDecimal) extends SymbolFilter

    case class MaxNumOrders(symbolFilter: SymbolFilter,
                            maxNumOrders: Long) extends SymbolFilter

    case class MaxNumAlgoOrders(symbolFilter: SymbolFilter,
                                maxNumAlgoOrders: Long) extends SymbolFilter

    case class MaxNumIcebergOrders(symbolFilter: SymbolFilter,
                                   maxNumIcebergOrders: Long) extends SymbolFilter

    case class MaxPositionalFilter(symbolFilter: SymbolFilter,
                                   maxPosition: BigDecimal) extends SymbolFilter
  }

  sealed trait ExchangeFilter

  object ExchangeFilter {
    case class ExchangeMaxNumOrders(filterType: String, maxNumOrders: Long) extends ExchangeFilter

    case class ExchangeMaxNumAlgoOrders(filterType: String, maxNumAlgoOrders: Long) extends ExchangeFilter
  }

}
