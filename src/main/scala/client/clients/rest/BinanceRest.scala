package client.clients.rest

trait BinanceRest[F[_]] {

  protected val baseEndpoint = "https://api.binance.com"
  protected val api = "/api/v3"

}
