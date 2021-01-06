package $package$.service.authorization.impl

import cats.effect.IO
import $package$.endpoints.models._
import $package$.service.authorization._

private[authorization] class AuthorizationStatic() extends AuthorizationModule.Service {
  override def authenticate(
    authorizationToken: AuthorizationToken
  ): IO[Either[AuthorizationModule.Error, UserId]] = {
    val result = authorizationToken match {
      case AuthorizationToken("this-is-the-valid-token") =>
        Right(UserId("this-is-the-valid-user"))
      case _ =>
        Left(AuthorizationModule.Unauthorized)
    }
    IO.pure(result)
  }
}
