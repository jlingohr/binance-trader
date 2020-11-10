package client.domain.orders.http

import java.time.Instant

import client.domain.params._
import client.domain.symbols.Symbol
import io.estatico.newtype.macros.newtype

object OrderResponse {

  @newtype case class OrderListId(value: Long)

  case class OrderAck(symbol: Symbol,
                      orderId: OrderId,
                      orderListId: OrderListId,
                      clientOrderId: ClientOrderId,
                      transactionTime: Instant)

  case class Result(price: Price,
                    origQty: Quantity,
                    executedQty: Quantity,
                    cummulativeQuoteQty: Quantity,
                    status: OrderStatus,
                    timeInForce: TimeInForce,
                    orderType: OrderType,
                    side: Side)

  @newtype case class CommissionAsset(value: String)

  case class Fill(price: Price, qty: Quantity, commission: Price, commissionAsset: CommissionAsset)

  case class OrderFull(ack: OrderAck, result: Result, fills: Seq[Fill])

  case class Order(symbol: Symbol,
                  orderId: OrderId,
                  orderListId: OrderListId,
                  clientOrderId: ClientOrderId,
                  price: Price,
                  origQty: Quantity,
                  executedQty: Quantity,
                  cummulativeQuoteQty: Quantity,
                  status: OrderStatus,
                  timeInForce: TimeInForce,
                  orderType: OrderType,
                  side: Side,
                  stopPrice: Price,
                  icebergQty: Quantity,
                  time: Instant,
                  updateTime: Instant,
                  isWorking: Boolean,
                  origQuoteOrderQty: Quantity)

  case class CancelOrder(symbol: Symbol,
                        origClientId: TradeId,
                        orderId: OrderId,
                        orderListId: OrderListId,
                        clientOrderId: OrderId,
                        price: Price,
                        origQty: Quantity,
                        executedQty: Quantity,
                        cummulativeQuoteQty: Quantity,
                        status: OrderStatus,
                        timeInForce: TimeInForce,
                        orderType: OrderType,
                        side: Side)

  case class OpenOrder(symbol: Symbol,
                      orderId: OrderId,
                      orderListId: OrderListId,
                      clientOrderId: OrderId,
                      price: Price,
                      origQty: Quantity,
                      executedQty: Quantity,
                      cummulativeQuoteQty: Quantity,
                      status: OrderStatus,
                      timeInForce: TimeInForce,
                      orderType: OrderType,
                      side: Side,
                      stopPrice: Price,
                      icebergQty: Quantity,
                      time: Instant,
                      updateTime: Instant,
                      isWorking: Boolean,
                      origQuoteOrderQty: Quantity)

  case class StatusResponse(symbol: Symbol,
                            orderId: OrderId,
                            orderListId: OrderListId,
                            clientOrderId: OrderId,
                            price: Price,
                            origQty: Quantity,
                            executedQty: Quantity,
                            cummulativeQuoteQty: Quantity,
                            status: OrderStatus,
                            timeInForce: TimeInForce,
                            orderType: OrderType,
                            side: Side,
                            stopPrice: Price,
                            icebergQty: Quantity,
                            time: Instant,
                            updateTime: Instant,
                            isWorking: Boolean,
                            origQuoteQty: Quantity)

}
