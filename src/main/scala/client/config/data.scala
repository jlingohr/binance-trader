package client.config

object data {

  case class HttpClientConfig(connectTimeout: Long,
                               requestTimeout: Long)

}
