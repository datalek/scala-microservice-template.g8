package io.rakhov.exampleservice

import io.rakhov.exampleservice.services.impl._

object ConfigProvider {
  def parseConfigServiceA(config: Config): Either[String, ConfigServiceA]
  def parseConfigServiceB(config: Config): Either[String, ConfigServiceB]
}