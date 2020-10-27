package client.domain

import java.time.Instant

import client.domain.events.{Price, Quantity, Symbol, UpdateId}

object depths {

  case class Bid(priceLevel: Price, quantity: Quantity)
  case class Ask(priceLevel: Price, quantity: Quantity)

  case class PartialBookDepth(lastUpdatedId: UpdateId,
                             bids: Seq[Bid],
                             asks: Seq[Ask]) extends Event

  case class DepthUpdate(eventTime: Instant,
                        symbol: Symbol,
                        firstUpdateId: UpdateId,
                        finalUpdateId: UpdateId,
                        bids: Seq[Bid],
                        asks: Seq[Ask]) extends Event

}
