package io.rakhov.exampleservice.endpoints

import cats.effect._
import org.http4s.server.blaze.BlazeServerBuilder
import scala.concurrent.duration._

object Server {
  /* boilerplate code */
  def run(
    hostname : String,
    port     : Int,
    endpoints: Endpoints
  )(
    implicit
    concurrentEffect: ConcurrentEffect[IO],
    timer           : Timer[IO]
  ): IO[Nothing] = {
    BlazeServerBuilder[IO](scala.concurrent.ExecutionContext.Implicits.global)
      .bindHttp(port, hostname)
      .withHttpApp(Endpoints.endpoints(endpoints))
      .withResponseHeaderTimeout(3.seconds)
      .resource
      .use(_ => IO.never)
  }
}
