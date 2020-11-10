package client.http

import java.net.http.HttpClient
import java.time.Duration

import cats.effect.IO
import client.clients.BinanceRestClient
import client.config.data.HttpClientConfig
import client.domain.depths.http.DepthLimit.Depth5
import client.domain.klines.KlineInterval.Daily
import client.domain.symbols
import org.http4s.client.jdkhttpclient.JdkHttpClient
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should.Matchers

import scala.concurrent.ExecutionContext

class PublicClientSpec extends AnyFunSpec with Matchers {

  implicit val cs = IO.contextShift(ExecutionContext.global)
  implicit val tm = IO.timer(ExecutionContext.global)

  private val conf = HttpClientConfig(20, 20)
  val client = HttpClient
    .newBuilder()
    .connectTimeout(Duration.ofSeconds(conf.connectTimeout))
    .build()

  private val restClient =
    BinanceRestClient
    .mkPublicClient[IO](
      JdkHttpClient(client)
    )

  val symbol = symbols.Symbol("BTCUSDT")

  describe("Testing general client") {

    it("Should successfully ping servers") {
      noException should be thrownBy restClient.generalClient.ping.unsafeRunSync()
    }

    it("should successfully get server time") {
      noException should be thrownBy restClient.generalClient.time.unsafeRunSync()
    }

    it("Should successfully obtain exchange info") {
      noException should be thrownBy restClient.generalClient.exchangeInfo.unsafeRunSync()
    }
  }

  describe("Testing MarketClient implementation") {

    it("Should successfully query average price") {
      noException should be thrownBy restClient.marketClient.avgPrice(symbol).unsafeRunSync()
    }

    it("Should properly query depths") {
      noException should be thrownBy restClient.marketClient.depth(symbol, Depth5).unsafeRunSync()
    }

    it("Should properly query klines") {
      noException should be thrownBy restClient.marketClient.klines(symbol, Daily).unsafeRunSync()
    }

    it("Should properly query trades") {
      noException should be thrownBy restClient.marketClient.trades(symbol).unsafeRunSync()
    }

    it("Should properly query aggregate trades") {
      noException should be thrownBy restClient.marketClient.aggTrades(symbol).unsafeRunSync()
    }

  }

  describe("Testing ticker client") {
    it("Should properly query book ticker for single currency") {
      noException should be thrownBy restClient.tickerClient.bookTicker(symbol)
    }

    it("Should properly query 24 hour ticker price") {
      noException should be thrownBy restClient.tickerClient.ticker24(symbol)
    }

    it("Should properly query ticker price") {
      noException should be thrownBy restClient.tickerClient.price(symbol)
    }
  }





}
