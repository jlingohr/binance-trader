package client.domain

import java.time.Instant

import client.domain.events.{OrderId, Price, Quantity, Symbol, TradeId}



object trades {

  case class trades(eventTime: Instant,
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