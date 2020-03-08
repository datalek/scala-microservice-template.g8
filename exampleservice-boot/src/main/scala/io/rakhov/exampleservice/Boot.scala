package io.rakhov.exampleservice

import zio._
import com.typesafe.config._
import io.rakhov.exampleservice.api.impl._
import io.rakhov.exampleservice.http.Server
import io.rakhov.exampleservice.services.authorizations.impl._
import io.rakhov.exampleservice.services.clients.impl._
import io.rakhov.exampleservice.services.events.impl._

object Boot extends App {

  def run(args: List[String]): URIO[Any, Int] =
    server.fold(throwable => { println(throwable); 1 }, _ => 0)

  val server = for {
    // parse configurations
    config <- ZIO.succeed(ConfigFactory.load())
    authorizationServiceConf <- AuthorizationsImplConf.load(config.getConfig("settings"))
    (host, port) <- ZIO.fromTry(scala.util.Try("127.0.0.1", 8080))

    // create services
    clientService = new ClientsStub()
    eventService = new EventServiceNoOp()
    authorizationService = new AuthorizationsImpl(
      backingStore = authorizationServiceConf
    )

    // initiate the api handler
    clientApi = new ClientApiImpl(
      authorizationService = authorizationService,
      clientService = clientService
    )

    // initiate the server
    server = Server.startServer(clientApi, eventService)(host = host, port = port)
  } yield server

}