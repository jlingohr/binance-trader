package client.ws

import java.net.http.HttpClient
import java.net.http.HttpClient.Version
import java.time.Duration

import cats.effect.IO
import client.clients.websocket.BinanceConnectionLive
import client.domain.depths.depths
import client.domain.depths.depths.DepthLevel
import client.domain.klines.ws.klines.{KlineEvent, KlineInterval}
import client.domain.symbols.Symbol
import client.domain.tickers.ws.tickers
import client.domain.tickers.ws.tickers.SymbolMiniTicker
import client.domain.trades.ws.trades
import client.domain.ws.requests._
import client.domain.ws.{BinanceWSRequest, Event}
import io.circe.Error
import org.http4s.client.jdkhttpclient.JdkWSClient
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.ExecutionContext

object BinanceWSClientSpec {
  implicit val cs = IO.contextShift(ExecutionContext.global)
  implicit val tm = IO.timer(ExecutionContext.global)

  val httpClient = HttpClient
    .newBuilder()
    .version(Version.HTTP_2)
    .connectTimeout(Duration.ofSeconds(20))
    .build()

  val wsClient = BinanceConnectionLive.apply[IO](JdkWSClient(httpClient))
}

class BinanceWSClientSpec
  extends AnyFunSpec
  with Matchers {

  import BinanceWSClientSpec._

  private val symbol = Symbol("BTCUSDT")

  def makeRequest(request: BinanceWSRequest, message: PartialFunction[Either[Error, Event], Event]): Vector[Event] = {
    wsClient
      .connect(Set(request))
      .use { stream =>
        stream
          .take(2)
          .collect(message)
          .compile
          .toVector
      }.unsafeRunSync()
  }

  describe("Test deserializing websocket responses") {
    it("Should deserialize AggTrade") {
      val result = makeRequest(AggTrade(symbol), { case Right(event: trades.AggTrade) => event })

      result.size shouldBe 2
    }

    it("Should deserialize Trade") {
      val result =
        makeRequest(Trade(symbol), { case Right(event: trades.Trade) => event })

      result.size shouldBe 2
    }

    it("Should deserialize KlineEvent") {
      val result =
        makeRequest(Kline(symbol, KlineInterval("1m")), { case Right(event: KlineEvent) => event })

      result.size shouldBe 2
    }

    it("Should deserialize MiniTicker") {
      val result =
        makeRequest(MiniTicker(symbol), { case Right(event: SymbolMiniTicker) => event })

      result.size shouldBe 2
    }

    it("Should deserialize SymbolMiniTicker") {
      val result =
        makeRequest(SymbolTicker(symbol), { case Right(event: tickers.SymbolTicker) => event })

      result.size shouldBe 2
    }

    it("Should deserialize SymbolBookTicker") {
      val result =
        makeRequest(SymbolBookTicker(symbol), { case Right(event: tickers.SymbolBookTicker) => event })

      result.size shouldBe 2
    }

    it("Should deserialize DepthUpdate") {
      val result =
        makeRequest(PartialBookDepth(symbol, DepthLevel(5)), { case Right(event: depths.PartialDepthUpdate) => event})

      result.size shouldBe 2
    }
  }

}
