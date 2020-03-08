package io.rakhov.exampleservice

import cats.effect._
import io.rakhov.exampleservice.service._
import io.rakhov.exampleservice.endpoints._
import io.rakhov.exampleservice.service.authorization._
import io.rakhov.exampleservice.service.clients._
import io.rakhov.exampleservice.service.configurations._
import io.rakhov.exampleservice.service.events._

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {

    for {
      logger <- IO(org.log4s.getLogger)

      _ <- IO(logger.info("Parse configuration."))
      config <- ConfigModule.loadConfig()

      _ <- IO(logger.info("Prepare the environment."))
      clientService <- ClientModule.Live()
      authorizationService <- AuthorizationModule.Live()
      eventService <- EventModule.Live()

      _ <- IO(logger.info("Create endpoints."))
      endpoints <- ClientEndpointsModule.endpoints(clientService, authorizationService, eventService, config)

      _ <- IO(logger.info("Start the server."))
      _ <- Server.run(config.hostname, config.port, endpoints)

      _ <- IO(logger.info("Exit."))
    } yield ExitCode.Success

  }

}
