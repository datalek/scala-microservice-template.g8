package $package$.service.configurations

import cats.effect._
import scala.util._

object ConfigModule {

  def loadConfig(): IO[Config] = {
    def safe[U](f: => U): IO[U] =
      IO.fromTry(Try(f))
    for {
      config <- IO(com.typesafe.config.ConfigFactory.load())
      hostname <- safe(config.getString("server.http.host"))
      port <- safe(config.getInt("server.http.port"))
      result = Config(
        hostname = hostname,
        port = port)
    } yield result
  }

}
