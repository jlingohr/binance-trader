package client.domain.http

import enumeratum.{CirceEnum, EnumEntry, Enum}

sealed trait RateLimitInterval extends EnumEntry

object RateLimitInterval extends Enum[RateLimitInterval] with CirceEnum[RateLimitInterval] {
  final case object SECOND extends RateLimitInterval
  final case object MINUTE extends RateLimitInterval
  final case object DAY extends RateLimitInterval

  val values = findValues
}

sealed trait RateLimitType extends EnumEntry

object RateLimitType extends Enum[RateLimitType] with CirceEnum[RateLimitType] {
  final object REQUEST_WEIGHT extends RateLimitType
  final object ORDERS extends RateLimitType
  final object RAW_REQUESTS extends RateLimitType

  val values = findValues
}

case class RateLimiter(rateLimitType: RateLimitType,
                      rateLimitInterval: RateLimitInterval,
                      intervalNum: Long,
                      limit: Long)

