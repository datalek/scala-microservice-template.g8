package $package$.services.impl

import $package$.services._

case class ConfigServiceB(
  a: Int,
  b: Int
)

case class ServiceBImpl(
  config: ConfigServiceB
) extends ServiceB {
  def doStuff(in: String): Either[ErrorOfB, String] =
    ???
}