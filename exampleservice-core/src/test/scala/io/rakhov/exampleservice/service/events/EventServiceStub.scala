package io.rakhov.exampleservice.service.events

import io.rakhov.exampleservice.services.events._
import zio._

private[events] class EventServiceStub extends Events.Service {

  private var internalEvents: List[Event] = Nil

  def insert(definition: EventDefinition): UIO[Event] = synchronized {
    val event = Event(
      id = EventId(internalEvents.length.toString),
      subject = definition.subject,
      action = definition.action
    )
    internalEvents = event :: internalEvents
    ZIO.succeed(event)
  }

  def events: UIO[List[Event]] =
    ZIO.succeed(internalEvents)

}

object EventServiceStub {
  def apply(): EventServiceStub =
    new EventServiceStub()
}
