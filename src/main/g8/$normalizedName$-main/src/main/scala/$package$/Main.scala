package $package$

import cats.effect._
import $package$.service._
import $package$.endpoints._
import $package$.service.authorization._
import $package$.service.clients._
import $package$.service.configurations._
import $package$.service.events._

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
