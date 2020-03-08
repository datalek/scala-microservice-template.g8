package io.rakhov.exampleservice.services.events

import io.rakhov.exampleservice.services.clients._
import io.rakhov.exampleservice.services.authorizations._

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

sealed trait Action {
  def date: java.time.Instant
}
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
