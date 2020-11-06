package client.clients.rest.client

import client.domain.http.response

trait GeneralClient[F[_]] {

  def ping: F[response]

  def time: F[response]

  def exchangeInfo: F[response]

}
