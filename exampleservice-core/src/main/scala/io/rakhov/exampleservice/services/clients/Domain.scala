package io.rakhov.exampleservice.services.clients

import io.rakhov.exampleservice.services.authorizations._

final case class TenantId(
  value: String
) extends AnyVal

final case class ClientId(
  value: String
)
object ClientId {
  def from(tenant: TenantId, segment: String): ClientId =
    ClientId(s"${tenant.value}::$segment")
}

final case class ClientDefinition(
  tenantId: TenantId,
  name: String,
  owner: Identity,
  scope: Seq[String]
)
object ClientDefinition {
  type PostponeOwnerProvisioning = Identity => ClientDefinition
}

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
