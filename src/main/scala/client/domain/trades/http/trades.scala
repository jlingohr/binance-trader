package client.domain.trades.http

import java.time.Instant

import client.domain.params.{Price, Quantity, TradeId}

object trades {

  case class Trade(id: TradeId,
                   price: Price,
                   qty: Quantity,
                   quoteQty: Quantity,
                   time: Instant,
                   isBuyerMaker: Boolean,
                   isBestMatch: Boolean)

  case class AggTrade(tradeId: TradeId,
                     price: Price,
                     qty: Quantity,
                     firstTradeId: TradeId,
                     lastTradeId: TradeId,
                     timestamp: Instant,
                     isBuyerMaker: Boolean,
                     bestMatch: Boolean)

}
