package client.clients.rest.client

import java.time.Instant

import client.domain.orders.http.{OrderType, Side, TimeInForce}
import client.domain.params.{OrderId, Price, Quantity}
import org.http4s.QueryParamEncoder

object QueryParams {

  object QueryEncoder {
    implicit val bigDecimalEncoder: QueryParamEncoder[BigDecimal] = QueryParamEncoder[String].contramap(_.toString())
    implicit val sideEncoder: QueryParamEncoder[Side] = QueryParamEncoder[String].contramap(_.entryName)
    implicit val orderTypeEncoder: QueryParamEncoder[OrderType] = QueryParamEncoder[String].contramap(_.entryName)
    implicit val instantEncoder: QueryParamEncoder[Instant] = QueryParamEncoder[String].contramap(_.toEpochMilli.toString)
    implicit val timeInForceEncoder: QueryParamEncoder[TimeInForce] = QueryParamEncoder[String].contramap(_.toString)
    implicit val priceEncoder: QueryParamEncoder[Price] = QueryParamEncoder[BigDecimal].contramap(_.value)
    implicit val quantityEncoder: QueryParamEncoder[Quantity] = QueryParamEncoder[BigDecimal].contramap(_.value)
    implicit val orderIdEncoder: QueryParamEncoder[OrderId] = QueryParamEncoder[Long].contramap(_.value)
  }

}
