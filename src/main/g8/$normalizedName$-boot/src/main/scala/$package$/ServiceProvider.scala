package $package$

import $package$.services._
import $package$.services.impl._

object ServiceProvider {

  def serviceA(config: ConfigServiceA): ServiceA =
    ServiceAImpl(config)

  def serviceB(config: ConfigServiceB): ServiceB =
    ServiceBImpl(config)

}