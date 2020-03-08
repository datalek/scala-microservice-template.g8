package io.rakhov.exampleservice.services.events.impl

import io.rakhov.exampleservice.services.events._
import zio._

class EventServiceNoOp extends Events.Service {
  def insert(definition: EventDefinition): UIO[Event] = {
    val event = Event(
      id = EventId(scala.util.Random.nextString(10)),
      subject = definition.subject,
      action = definition.action
    )
    ZIO.succeed(event)
  }
}
