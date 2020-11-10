package client.domain.account.http

import client.domain.params.Asset
import io.estatico.newtype.macros.newtype

object Account {

  @newtype case class AccountType(value: String)

  case class Balance(asset: Asset, free: BigDecimal, locked: BigDecimal)

  @newtype case class AccountPermission(value: String)

}
