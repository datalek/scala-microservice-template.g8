package io.rakhov.exampleservice.service.events.impl

import cats.effect.IO
import io.rakhov.exampleservice.service.events.{Event, EventModule}

private[events] class EventPrintln() extends EventModule.Service {
  override def sendEvent(
    event: Event
  ): IO[Event] = {
    def run() = {
      println(event)
      event
    }
    IO(run())
  }
}
