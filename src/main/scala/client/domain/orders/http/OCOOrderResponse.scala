package client.domain.orders.http

import java.time.Instant

import client.domain.orders.http.OrderResponse.OrderListId
import client.domain.params.{ClientOrderId, OrderId, Price, Quantity}
import client.domain.symbols.Symbol

object OCOOrderResponse {

  case class OCODetail(symbol: Symbol,
                      orderId: OrderId,
                      clientOrderId: ClientOrderId)

  case class OCOReport(symbol: Symbol,
                      orderId: OrderId,
                      orderListId: OrderListId,
                      clientOrderId: ClientOrderId,
                      transactionTime: Instant,
                      price: Price,
                      origQty: Quantity,
                      executedQty: Quantity,
                      cummulativeQuoteQty: Quantity,
                      status: OrderStatus,
                      timeInForce: TimeInForce,
                      orderType: OrderType,
                      side: Side,
                      stopPrice: Option[Price])

  case class OCOOrder(orderListId: OrderListId,
                     contingencyType: ContingencyType,
                     listStatusType: ListStatusType,
                     listOrderStatus: ListOrderStatus,
                     listClientOrderId: ClientOrderId,
                     transactionTime: Instant,
                     symbol: Symbol,
                     orders: Seq[OCODetail],
                     orderReports: Seq[OCOReport])

  case class CancelOCO(orderListId: OrderListId,
                      contingencyType: ContingencyType,
                      listStatusType: ListStatusType,
                      listClientOrderId: ClientOrderId,
                      transactionTime: Instant,
                      symbol: Symbol,
                      orders: Seq[OCODetail],
                      orderReports: Seq[OCOReport])

}
