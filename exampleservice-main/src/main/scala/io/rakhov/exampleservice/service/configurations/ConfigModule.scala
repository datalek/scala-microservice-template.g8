package io.rakhov.exampleservice.service.configurations

import cats.effect._
import com.typesafe.config._

import scala.util._

object ConfigModule {

  def loadConfig(): IO[Config] = {
    def safe[U](f: => U): IO[U] =
      IO.fromTry(Try(f))
    for {
      config <- IO(ConfigFactory.load())
      hostname <- safe(config.getString("settings.server.http.host"))
      port <- safe(config.getInt("settings.server.http.port"))
      result = Config(
        hostname = hostname,
        port = port)
    } yield result
  }

}
