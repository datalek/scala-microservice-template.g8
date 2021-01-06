package $package$.service.authorization

import cats.effect._
import $package$.endpoints.models._
import $package$.service.authorization.impl._

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
