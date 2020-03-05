package io.rakhov.exampleservice.services.authorizations

final case class UserId(
  value: String
) extends AnyVal

sealed trait Identity
object Identity {
  final case class User(
    id: UserId
  ) extends Identity
}

sealed trait Authorization
object Authorization {
  final case class Bearer(
    value: String
  ) extends Authorization
}

sealed trait AuthorizationError
object AuthorizationError {
  final case class Expired(authorization: Authorization)
}