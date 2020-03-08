package io.rakhov.exampleservice.http

import scala.io._
import akka.actor._
import akka.http.scaladsl._
import akka.stream._
import io.rakhov.exampleservice.api.ClientApi
import io.rakhov.exampleservice.services.events.Events

object Server {

  def startServer(api: ClientApi.Service, eventService: Events.Service)(host: String, port: Int): Unit = {

    implicit val system = ActorSystem("my-system")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val bindingFuture = Http().bindAndHandle(
      handler = ClientApiRoutes.route(api, eventService),
      interface = host,
      port = port
    )

    println(s"Server online at http://$host:$port/\nPress RETURN to do nothing...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }

}
