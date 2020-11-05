package client.algebra

import cats.Id
import client.domain.Credentials
import org.http4s.{Request, Uri}
import tsec.common._
import tsec.mac.jca.{HMACSHA256, MacSigningKey}

trait SecurityService[F[_]] {

  def sign(uri: Uri): Uri
  def putHeader(uri: Uri): Uri

}

class LiveSecurityService[F[_]](credentials: Credentials, signedUri: Uri) extends SecurityService[F] {

  private val header = "X-MBX-APIKEY"

  def sign(uri: Uri): Uri =
    signedUri
      .withPath(uri.path)

  override def putHeader(uri: Uri): Uri =
    uri
      .withQueryParam(header, credentials.key)
}

object LiveSecurityService {
  def apply[F[_]](credentials: Credentials): Request[F] => Either[Throwable, LiveSecurityService[F]] = (request: Request[F]) => {
    val signingKey = HMACSHA256.buildKey[Id](credentials.secret.asciiBytes)
    sign(request.queryString, signingKey)
      .map(signature => request.uri.withQueryParam("signature", signature))
      .map(uri => new LiveSecurityService[F](credentials, uri))
  }

  def sign(plainText: String, signingKey: MacSigningKey[HMACSHA256]) =
    HMACSHA256.sign(plainText.asciiBytes, signingKey).map(_.toHexString)
}