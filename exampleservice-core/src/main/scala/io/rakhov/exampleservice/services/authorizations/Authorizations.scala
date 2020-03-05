package io.rakhov.exampleservice.services.authorizations

import io.rakhov.exampleservice.services.authorizations.AuthorizationError.Expired
import zio._

object Authorizations {
  trait Service {
    def authorize(authorization: Authorization): UIO[Either[Expired, Identity]]
  }
}
