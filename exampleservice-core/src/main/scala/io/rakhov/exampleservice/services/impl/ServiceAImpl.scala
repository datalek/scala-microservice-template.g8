package io.rakhov.exampleservice.services.impl

import io.rakhov.exampleservice.services._

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