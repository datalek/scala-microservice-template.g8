package io.rakhov.exampleservice

import io.rakhov.exampleservice.services._
import io.rakhov.exampleservice.services.impl._

object ServiceProvider {

  def serviceA(config: ConfigServiceA): ServiceA =
    ServiceAImpl(config)

  def serviceB(config: ConfigServiceB): ServiceB =
    ServiceBImpl(config)

}