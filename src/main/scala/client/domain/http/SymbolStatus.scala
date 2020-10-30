package client.domain.http

sealed trait SymbolStatus

object SymbolStatus {

  final case object PRE_TRADING extends SymbolStatus
  final case object TRADING extends SymbolStatus
  final case object POST_TRADING extends SymbolStatus
  final case object END_OF_DAY extends SymbolStatus
  final case object HALT extends SymbolStatus
  final case object AUCTION_MATCH extends SymbolStatus
  final case object BREAK extends SymbolStatus

}
