package io.rakhov.exampleservice.service.events

import io.rakhov.exampleservice.endpoints.models._
import java.time.Instant

sealed trait Event
object Event {

  final case class CreateEvent(
    client: Client,
    createDate: Instant
  ) extends Event

  final case class UpdateEvent(
    update: ClientUpdate,
    updated: Client,
    createDate: Instant
  ) extends Event

}
