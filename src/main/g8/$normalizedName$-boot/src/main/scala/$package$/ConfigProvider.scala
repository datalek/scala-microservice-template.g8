package $package$

import $package$.services.impl._

object ConfigProvider {
  def parseConfigServiceA(config: Config): Either[String, ConfigServiceA]
  def parseConfigServiceB(config: Config): Either[String, ConfigServiceB]
}