package client.domain.account.http

import java.time.Instant

import client.domain.account.http.Account.{AccountPermission, AccountType, Balance}
import client.domain.orders.http.OrderResponse.{CommissionAsset, OrderListId}
import client.domain.params._
import client.domain.symbols.Symbol

object AccountResponse {

  case class AccountInformation(marketCommission: Commission,
                               takerCommission: Commission,
                               buyerCommission: Commission,
                               sellerCommission: Commission,
                               canTrade: Boolean,
                               canWithdraw: Boolean,
                               canDeposit: Boolean,
                               updateTime: Instant,
                               accountType: AccountType,
                               balance: Seq[Balance],
                               permissions: Seq[AccountPermission])

  case class AccountTrade(symbol: Symbol,
                         id: TradeId,
                         orderId: OrderId,
                         orderListId: OrderListId,
                         price: Price,
                         qty: Quantity,
                         quoteQty: Quantity,
                         commission: Price,
                         commissionAsset: CommissionAsset,
                         time: Instant,
                         isBuyer: Boolean,
                         isMaker: Boolean,
                         isBestMatch: Boolean)

}
