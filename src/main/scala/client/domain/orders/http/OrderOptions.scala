package client.domain.orders.http

import client.domain.orders.http.OrderOptions.Market.MarketOrderQuantity
import client.domain.params.{Price, Quantity}
import org.http4s.Uri


sealed trait OrderOptions {
  def withUri(uri: Uri): Uri
}

object OrderOptions {
  import client.clients.rest.client.QueryParams.QueryEncoder._

  case class Limit(timeInForce: TimeInForce, quantity: Quantity, price: Price) extends OrderOptions {
    def withUri(uri: Uri): Uri =
      uri
        .withQueryParam("timeInForce", timeInForce)
        .withQueryParam("quantity", quantity)
        .withQueryParam("price", price)
  }


  case class Market(quantity: MarketOrderQuantity) extends OrderOptions {
    override def withUri(uri: Uri): Uri = quantity.withUri(uri)
  }


  object Market {
    sealed trait MarketOrderQuantity extends OrderOptions { val value: Quantity }

    case class MarketQuantity(value: Quantity) extends MarketOrderQuantity {
      override def withUri(uri: Uri): Uri =
        uri
          .withQueryParam("quantity", value)
    }

    case class QuoteOrderQty(value: Quantity) extends MarketOrderQuantity {
      override def withUri(uri: Uri): Uri =
        uri
          .withQueryParam("quoteOrderQrt", value)
    }

  }

  case class StopLoss(quantity: Quantity, stopPrice: Price) extends OrderOptions {
    override def withUri(uri: Uri): Uri =
      uri
        .withQueryParam("quantity", quantity)
        .withQueryParam("stopPrice", stopPrice)
  }

  case class StopLossLimit(timeInForce: TimeInForce, quantity: Quantity, price: Price, stopPrice: Price) extends OrderOptions {
    override def withUri(uri: Uri): Uri =
      uri
        .withQueryParam("timeInForce", timeInForce)
        .withQueryParam("quantity", quantity)
        .withQueryParam("price", price)
        .withQueryParam("stopPrice", stopPrice)
  }

  case class TakeProfit(quantity: Quantity, stopPrice: Price) extends OrderOptions {
    override def withUri(uri: Uri): Uri =
      uri
        .withQueryParam("quantity", quantity)
        .withQueryParam("stopPrice", stopPrice)
  }

  case class TakeProfitLimit(timeInForce: TimeInForce, quantity: Quantity, price: Price, stopPrice: Price) extends OrderOptions {
    override def withUri(uri: Uri): Uri =
      uri
        .withQueryParam("timeInForce", timeInForce)
        .withQueryParam("quantity", quantity)
        .withQueryParam("price", price)
        .withQueryParam("stopPrice", stopPrice)
  }


  case class LimitMaker(quantity: Quantity, price: Price) extends OrderOptions {
    override def withUri(uri: Uri): Uri =
      uri
        .withQueryParam("quantity", quantity)
        .withQueryParam("price", price)
  }


}
