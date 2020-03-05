package io.rakhov.exampleservice.services.events

import io.rakhov.exampleservice.api._

final case class EventId(
  value: String
) extends AnyVal

final case class EventDefinition(
  subject: Identity,
  action: Action
)

final case class Event(
  id: EventId,
  subject: Identity,
  action: Action
)

sealed trait Action
object Action {
  final case class ClientUpdated(
    client: ClientId,
    date: java.time.Instant
  ) extends Action
  final case class ClientCreated(
    client: ClientId,
    date: java.time.Instant
  ) extends Action
}
