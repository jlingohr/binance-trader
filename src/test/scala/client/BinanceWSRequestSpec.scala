package client

import client.domain.depths.depths.DepthLevel
import client.domain.klines.ws.klines.KlineInterval
import client.domain.ws.requests.{AggTrade, Depth, Kline, MiniTicker, PartialBookDepth, SymbolBookTicker, SymbolTicker, Trade}
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers
import client.domain.symbols.Symbol

class BinanceWSRequestSpec extends AnyFunSpec with Matchers {

  val symbol = Symbol("BNBBTC")

  describe("Test creating proper request uris") {
    it("Should create correct aggTrade request") {
      val request = AggTrade(symbol)
      val expected = "bnbbtc@aggTrade"

      request.streamName shouldEqual expected
    }

    it("Should create correct trade request") {
      val request = Trade(symbol)
      val expected = "bnbbtc@trade"

      request.streamName shouldEqual expected
    }

    it("Should create correct kline request") {
      val request = Kline(symbol, KlineInterval("1m"))
      val expected = "bnbbtc@kline_1m"

      request.streamName shouldEqual expected
    }

    it("Should create correct symbol mini ticker request") {
      val request = MiniTicker(symbol)
      val expected = "bnbbtc@miniTicker"

      request.streamName shouldEqual expected
    }

    it("Should create correct symbol ticker request") {
      val request = SymbolTicker(symbol)
      val expected = "bnbbtc@ticker"

      request.streamName shouldEqual expected
    }

    it("Should create correct symbol book ticker request") {
      val request = SymbolBookTicker(symbol)
      val expected = "bnbbtc@bookTicker"

      request.streamName shouldEqual expected
    }

    it("Should create correct partial book depth request") {
      val request = PartialBookDepth(symbol, DepthLevel(5))
      val expected = "bnbbtc@depth5"

      request.streamName shouldEqual expected
    }

    it("Should create correct depth update request") {
      val request = Depth(symbol)
      val expected = "bnbbtc@depth"

      request.streamName shouldEqual expected
    }
  }

}
