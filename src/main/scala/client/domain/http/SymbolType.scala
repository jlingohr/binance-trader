package client.domain.http

sealed trait SymbolType

object SymbolType {

  final case object SPOT extends SymbolType
}
