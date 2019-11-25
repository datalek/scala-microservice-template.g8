package $package$.services.impl

import $package$.services._

case class ConfigServiceA(
  a: Int,
  b: Int
)

case class ServiceAImpl(
  config: ConfigServiceA
) extends ServiceA {
  def doStuff(in: InputOfA): Either[ErrorOfA, OutputOfA] =
    ???
}