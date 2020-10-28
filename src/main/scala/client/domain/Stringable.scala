package client.domain

trait Stringable {

  type T

  val values: Set[T]

  val defaultValue: T

  val fromString: String => T = (s: String) =>
    values.find(_.toString == s) getOrElse defaultValue

}
