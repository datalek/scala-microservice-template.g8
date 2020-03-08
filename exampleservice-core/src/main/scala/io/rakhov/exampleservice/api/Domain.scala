package io.rakhov.exampleservice.api

import io.rakhov.exampleservice.services.clients._
import io.rakhov.exampleservice.services.authorizations._

sealed trait ClientApiError
object ClientApiError {
  final case class NotFound(
    clientId: ClientId
  ) extends ClientApiError
  final case class Unauthorized(
    authorization: Authorization
  ) extends ClientApiError
  final case class Forbidden(
    authorization: Authorization
  ) extends ClientApiError
}