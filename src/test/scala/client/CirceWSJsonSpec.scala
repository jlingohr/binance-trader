package client

import org.scalatest.funspec.AnyFunSpec
import java.time.Instant

import client.clients.websocket.CirceWSJson
import client.domain.depths.depths.{Ask, Bid, PartialDepthUpdate}
import io.circe.parser._
import client.domain.params.{AssetVolume, OrderId, Price, Quantity, TradeId, UpdateId}
import client.domain.klines.ws.klines.{Kline, KlineEvent, KlineInterval}
import client.domain.symbols.Symbol
import client.domain.ws.Event
import client.domain.tickers.ws.tickers.{OrderBookUpdateId, SymbolBookTicker, SymbolMiniTicker}
import client.domain.trades.ws.trades.{AggTrade, Trade}
import org.scalatest.matchers.should.Matchers


class CirceWSJsonSpec extends AnyFunSpec with Matchers with CirceWSJson {

  describe("Testing decoding websocket JSON response") {
    it("Should decode AggTrade") {
      val raw =
        """
          |{
          |  "e": "aggTrade",
          |  "E": 123456789,
          |  "s": "BNBBTC",
          |  "a": 12345,
          |  "p": "0.001",
          |  "q": "100",
          |  "f": 100,
          |  "l": 105,
          |  "T": 123456785,
          |  "m": true,
          |  "M": true
          |}
          |""".stripMargin

      val expected = AggTrade(
        Instant.ofEpochMilli(123456789),
        Symbol("BNBBTC"),
        TradeId(12345),
        Price(BigDecimal("0.001")),
        Quantity(BigDecimal("100")),
        TradeId(100),
        TradeId(105),
        Instant.ofEpochMilli(123456785),
        true)
      val result = decode[Event](raw)

      result.map(_ shouldEqual expected)

    }

    it("Should decode Trade") {
      val raw =
        """
          |{
          |  "e": "trade",     // Event type
          |  "E": 123456789,   // Event time
          |  "s": "BNBBTC",    // Symbol
          |  "t": 12345,       // Trade ID
          |  "p": "0.001",     // Price
          |  "q": "100",       // Quantity
          |  "b": 88,          // Buyer order ID
          |  "a": 50,          // Seller order ID
          |  "T": 123456785,   // Trade time
          |  "m": true,        // Is the buyer the market maker?
          |  "M": true         // Ignore
          |}
          |""".stripMargin

      val expected = Trade(
        Instant.ofEpochMilli(123456789),
        Symbol("BNBBTC"),
        TradeId(12345),
        Price(BigDecimal(0.001)),
        Quantity(BigDecimal("100")),
        OrderId(88),
        OrderId(50),
        Instant.ofEpochMilli(123456785),
        true
      )

      decode[Trade](raw).map(_ shouldEqual expected)
    }

    it("Should decode KlineEvent") {
      val raw =
        """
          |{
          |  "e": "kline",     // Event type
          |  "E": 123456789,   // Event time
          |  "s": "BNBBTC",    // Symbol
          |  "k": {
          |    "t": 123400000, // Kline start time
          |    "T": 123460000, // Kline close time
          |    "s": "BNBBTC",  // Symbol
          |    "i": "1m",      // Interval
          |    "f": 100,       // First trade ID
          |    "L": 200,       // Last trade ID
          |    "o": "0.0010",  // Open price
          |    "c": "0.0020",  // Close price
          |    "h": "0.0025",  // High price
          |    "l": "0.0015",  // Low price
          |    "v": "1000",    // Base asset volume
          |    "n": 100,       // Number of trades
          |    "x": false,     // Is this kline closed?
          |    "q": "1.0000",  // Quote asset volume
          |    "V": "500",     // Taker buy base asset volume
          |    "Q": "0.500",   // Taker buy quote asset volume
          |    "B": "123456"   // Ignore
          |  }
          |}
          |""".stripMargin

      val expected = KlineEvent(
        Instant.ofEpochMilli(123456789),
        Symbol("BNBBTC"),
        Kline(
          Instant.ofEpochMilli(123400000),
          Instant.ofEpochMilli(123460000),
          Symbol("BNBBTC"),
          KlineInterval("1m"),
          TradeId(100),
          TradeId(200),
          Price(BigDecimal(".0010")),
          Price(BigDecimal("0.0020")),
          Price(BigDecimal("0.0025")),
          Price(BigDecimal("0.0015")),
          AssetVolume(BigDecimal("1000")),
          100,
          false,
          AssetVolume(BigDecimal("1.0000")),
          AssetVolume(BigDecimal("500")),
          AssetVolume(BigDecimal("0.500"))
        )
      )

      decode[KlineEvent](raw).map(_ shouldEqual expected)
    }

    it("Should decode SymbolMiniTicker") {
      val raw =
        """
          |{
          |    "e": "24hrMiniTicker",  // Event type
          |    "E": 123456789,         // Event time
          |    "s": "BNBBTC",          // Symbol
          |    "c": "0.0025",          // Close price
          |    "o": "0.0010",          // Open price
          |    "h": "0.0025",          // High price
          |    "l": "0.0010",          // Low price
          |    "v": "10000",           // Total traded base asset volume
          |    "q": "18"               // Total traded quote asset volume
          |  }
          |""".stripMargin

      val expected = SymbolMiniTicker(
        Instant.ofEpochMilli(123456789),
        Symbol("BNBBTC"),
        Price(BigDecimal("0.0025")),
        Price(BigDecimal("0.0010")),
        Price(BigDecimal("0.0025")),
        Price(BigDecimal("0.0010")),
        AssetVolume(BigDecimal("10000")),
        AssetVolume(BigDecimal("18"))
      )

      decode[SymbolMiniTicker](raw).map(_ shouldEqual expected)
    }

    it("Should decode SymbolBookTicker") {
      val raw =
        """
          |{
          |  "u":400900217,     // order book updateId
          |  "s":"BNBUSDT",     // symbol
          |  "b":"25.35190000", // best bid price
          |  "B":"31.21000000", // best bid qty
          |  "a":"25.36520000", // best ask price
          |  "A":"40.66000000"  // best ask qty
          |}
          |""".stripMargin

      val expected = SymbolBookTicker(
        OrderBookUpdateId(400900217),
        Symbol("BNBUSDT"),
        Price(BigDecimal("25.35190000")),
        Quantity(BigDecimal("31.21000000")),
        Price(BigDecimal("25.36520000")),
        Quantity(BigDecimal("40.66000000"))
      )
    }

    it("Should decode PartialDepth") {
      val raw =
        """
          |{
          |  "lastUpdateId": 160,  // Last update ID
          |  "bids": [             // Bids to be updated
          |    [
          |      "0.0024",         // Price level to be updated
          |      "10"              // Quantity
          |    ]
          |  ],
          |  "asks": [             // Asks to be updated
          |    [
          |      "0.0026",         // Price level to be updated
          |      "100"            // Quantity
          |    ]
          |  ]
          |}
          |""".stripMargin

      val expected = PartialDepthUpdate(
        UpdateId(160),
        Seq(Bid(Price(BigDecimal("0.0024")), Quantity(BigDecimal("10")))),
        Seq(Ask(Price(BigDecimal("0.0026")), Quantity(BigDecimal("100"))))
      )

      decode[PartialDepthUpdate](raw).map(_ shouldEqual expected)
    }

    it("Should decode PartialDepth with multiple bids and asks") {
      val raw =
        """
          |{
          |  "lastUpdateId": 160,  // Last update ID
          |  "bids": [             // Bids to be updated
          |    [
          |      "0.0024",         // Price level to be updated
          |      "10"              // Quantity
          |    ],
          |    [
          |      "0.0024",         // Price level to be updated
          |      "10"              // Quantity
          |    ]
          |  ],
          |  "asks": [             // Asks to be updated
          |    [
          |      "0.0026",         // Price level to be updated
          |      "100"            // Quantity
          |    ],
          |    [
          |      "0.0026",         // Price level to be updated
          |      "100"            // Quantity
          |    ]
          |  ]
          |}
          |""".stripMargin

      val bid = Bid(Price(BigDecimal("0.0024")), Quantity(BigDecimal("10")))
      val ask = Ask(Price(BigDecimal("0.0026")), Quantity(BigDecimal("100")))
      val expected = PartialDepthUpdate(
        UpdateId(160),
        Seq(bid, bid),
        Seq(ask, ask)
      )

      decode[PartialDepthUpdate](raw).map(x => x shouldEqual expected)
    }


  }

}
