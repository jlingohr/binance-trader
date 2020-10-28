package client.domain

import client.domain.depths.DepthLevel
import client.domain.klines.KlineInterval
import client.domain.symbols.Pair

sealed trait BinanceWSRequest {
  def streamName: String
}

sealed trait SymbolRequest extends BinanceWSRequest {
  val symbol: Symbol
}

object requests {

  case class AggTrade(symbol: Symbol) extends SymbolRequest {
    def streamName = s"$symbol@aggTrade"
  }

  case class Trade(symbol: Symbol) extends SymbolRequest {
    def streamName = s"$symbol@trade"
  }

  case class Kline(symbol: Symbol, interval: KlineInterval) extends SymbolRequest {
    def streamName = s"$symbol@kline_$interval"
  }

  case class MiniTicker(symbol: Symbol) extends SymbolRequest {
    def streamName = s"$symbol@miniTicker"
  }

  case class AllMiniTicker() extends BinanceWSRequest {
    def streamName = s"!miniTicker@arr"
  }

  case class SymbolTicker(symbol: Symbol) extends SymbolRequest {
    def streamName = s"$symbol@ticker"
  }

  case class AllSymbolTicker() extends BinanceWSRequest {
    def streamName = s"!ticker@arr"
  }

  case class SymbolBookTicker(symbol: Symbol) extends SymbolRequest {
    def streamName = s"$symbol@bookTicker"
  }

  case class AllBookTicker() extends BinanceWSRequest {
    def streamName = s"!bookTicker"
  }

  case class PartialBookDepth(symbol: Symbol, levels: DepthLevel) extends SymbolRequest {
    def streamName = s"$symbol@depth$levels"
  }

  case class Depth(symbol: Symbol) extends SymbolRequest {
    def streamName = s"$symbol@depth"
  }
}
