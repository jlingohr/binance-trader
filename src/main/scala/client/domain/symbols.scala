package client.domain

import io.estatico.newtype.macros.newtype

object symbols {

  @newtype case class Symbol(value: String)

}
