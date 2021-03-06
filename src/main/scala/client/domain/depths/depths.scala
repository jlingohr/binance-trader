package client.domain.depths

import java.time.Instant

import client.domain.params.{Price, Quantity, UpdateId}
import client.domain.symbols.Symbol
import client.domain.ws.Event
import io.estatico.newtype.macros.newtype

object depths {

  @newtype case class DepthLevel(value: Int)

  //  @newtype case object DepthFive extends DepthLevel(5)
  //  @newtype case object DepthTen extends DepthLevel(10)
  //  @newtype case object DepthTwenty extends DepthLevel(20)

  case class Bid(priceLevel: Price, quantity: Quantity)

  case class Ask(priceLevel: Price, quantity: Quantity)

  case class PartialDepthUpdate(lastUpdatedId: UpdateId,
                                bids: Seq[Bid],
                                asks: Seq[Ask]) extends Event

  case class DepthUpdate(eventTime: Instant,
                         symbol: Symbol,
                         firstUpdateId: UpdateId,
                         finalUpdateId: UpdateId,
                         bids: Seq[Bid],
                         asks: Seq[Ask]) extends Event

}
