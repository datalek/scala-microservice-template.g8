package $package$.service.events.impl

import cats.effect.IO
import $package$.service.events._

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
