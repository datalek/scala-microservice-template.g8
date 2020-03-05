package io.rakhov.exampleservice.services.events

import zio._

object Events {
  trait Service {
    def insert(definition: EventDefinition): UIO[Event]
  }
}
