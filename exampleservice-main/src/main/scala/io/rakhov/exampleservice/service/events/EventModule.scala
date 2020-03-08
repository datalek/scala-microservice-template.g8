package io.rakhov.exampleservice.service.events

import cats.effect._
import io.rakhov.exampleservice.service.events.impl._

object EventModule {

  trait Service {
    def sendEvent(
      event: Event
    ): IO[Event]
  }

  def Live(): IO[EventModule.Service] =
    IO(new EventPrintln())

}
