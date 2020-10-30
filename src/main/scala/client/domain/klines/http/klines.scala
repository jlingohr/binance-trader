package client.domain.klines.http

import java.time.Instant

import client.domain.params.{AssetVolume, Price}

object klines {

  case class Kline(openTime: Instant,
                  open: Price,
                  high: Price,
                  low: Price,
                  close: Price,
                  volume: AssetVolume,
                  closeTime: Instant,
                  quoteAssetVolume: AssetVolume,
                  numTrades: Int,
                  takerBuyBaseAssetVolume: AssetVolume,
                  takerBuyQuoteAssetVolume: AssetVolume)

  object Kline {
    def apply(openTime: Instant,
              open: Price,
              high: Price,
              low: Price,
              close: Price,
              volume: AssetVolume,
              closeTime: Instant,
              quoteAssetVolume: AssetVolume,
              numTrades: Int,
              takerBuyBaseAssetVolume: AssetVolume,
              takerBuyQuoteAssetVolume: AssetVolume,
             ignore: Any): Kline = {

      new Kline(openTime: Instant,
                open: Price,
                high: Price,
                low: Price,
                close: Price,
                volume: AssetVolume,
                closeTime: Instant,
                quoteAssetVolume: AssetVolume,
                numTrades: Int,
                takerBuyBaseAssetVolume: AssetVolume,
                takerBuyQuoteAssetVolume: AssetVolume)
    }
  }

}
