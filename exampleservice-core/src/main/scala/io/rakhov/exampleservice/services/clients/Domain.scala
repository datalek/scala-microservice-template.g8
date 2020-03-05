package io.rakhov.exampleservice.services.clients

import io.rakhov.exampleservice.services.authorizations._

final case class TenantId(
  value: String
) extends AnyVal

final case class ClientId private(
  value: String
)
object ClientId {
  def apply(tenant: TenantId, id: String): ClientId =
    ClientId(s"${tenant.value}::$id")
}

final case class ClientDefinition(
  name: String,
  scope: Seq[String]
)

final case class ClientUpdate(
  name: String,
  scope: Seq[String]
)

final case class Client(
  id: ClientId,
  name: String,
  owner: Identity,
  scope: Seq[String]
)
