package client.clients

import cats.effect._
import cats.syntax.all._
import client.algebra.SecurityService
import client.clients.rest.client._
import client.clients.rest.client.interpreter._
import org.http4s.client.Client


trait BinanceRestClient[F[_]]

object BinanceRestClient {

  final class PublicClient[F[_]] private (val generalClient: GeneralClient[F],
                                          val marketClient: MarketClient[F],
                                          val tickerClient: TickerClient[F]) extends BinanceRestClient[F]

  object PublicClient {
    def make[F[_]: Sync](client: Client[F]) = {
      val generalClient = new LiveGeneralClient[F](client)
      val marketClient = new LiveMarketClient[F](client)
      val tickerClient = new LiveTickerClient[F](client)
      new PublicClient[F](generalClient, marketClient, tickerClient)
    }
  }

  final class SecureClient[F[_]] private (val generalClient: GeneralClient[F],
                                          val marketClient: MarketClient[F],
                                          val tickerClient: TickerClient[F],
                                          val orderClient: OrderClient[F],
                                          val ocoClient: OCOClient[F],
                                          val accountClient: AccountClient[F]) extends BinanceRestClient[F]

  object SecureClient {
    def make[F[_]: Sync](client: Client[F], security: SecurityService): F[SecureClient[F]] =
      for {
        generalClient <- LiveGeneralClient.make[F](client)
        marketClient <- LiveMarketClient.make[F](client)
        tickerClient <- LiveTickerClient.make[F](client)
        orderClient <- LiveOrderClient.make[F](client, security)
        ocoClient <- LiveOCOClient.make[F](client, security)
        accountClient <- LiveAccountClient.make[F](client, security)
      } yield new SecureClient[F](generalClient, marketClient, tickerClient, orderClient, ocoClient, accountClient)
  }

  def mkPublicClient[F[_]: Sync](client: Client[F]) = {
    PublicClient.make[F](client)

  }

}
