package $package$.service.events

import $package$.endpoints.models._
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
