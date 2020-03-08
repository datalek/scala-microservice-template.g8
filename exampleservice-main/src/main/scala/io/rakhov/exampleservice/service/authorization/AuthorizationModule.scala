package io.rakhov.exampleservice.service.authorization

import cats.effect._
import io.rakhov.exampleservice.endpoints.models._
import io.rakhov.exampleservice.service.authorization.impl._

object AuthorizationModule {

  sealed trait Error
  final object Unauthorized extends Error

  trait Service {
    def authenticate(
      authorizationToken: AuthorizationToken
    ): IO[Either[AuthorizationModule.Error, UserId]]
  }

  def Live(): IO[AuthorizationModule.Service] =
    IO(new AuthorizationStatic())

}
