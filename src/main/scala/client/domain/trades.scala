package client.domain

import java.time.Instant

import client.domain.events.{OrderId, Price, Quantity, TradeId}
import client.domain.symbols.Symbol



object trades {

  case class AggTrade(eventTime: Instant,
                    symbol: Symbol,
                    tradeId: TradeId,
                    price: Price,
                    quantity: Quantity,
                    firstTradeId: TradeId,
                    lastTradeId: TradeId,
                    tradeTime: Instant,
                    isBuyerMaker: Boolean
                   ) extends Event

  case class Trade(
                    eventTime: Instant,
                    symbol: Symbol,
                    tradeId: TradeId,
                    price: Price,
                    quantity: Quantity,
                    buyer: OrderId,
                    seller: OrderId,
                    tradeTime: Instant,
                    isBuyerMaker: Boolean
                  ) extends Event

}