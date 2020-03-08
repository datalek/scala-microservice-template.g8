package rio

import io.rakhov.exampleservice.endpoints.models._

final case class A()
final case class B()

object ClientsApi {

  trait CreateError
  trait RetrieveError

  def create(
    tenant: TenantId,
    authorization: AuthorizationToken,
    definition: ClientDefinition): RIO[A with B, Either[ClientsApi.CreateError, Client]] = {
    for {
      a <- ask[A]
      b <- ask[B]
      x = new CreateError {}
    } yield Left(x)
  }

  def retrieve(
    tenantId: TenantId,
    authorization: AuthorizationToken,
    clientId: ClientId): RIO[Unit, Either[ClientsApi.RetrieveError, Client]] = ???

  //runRIO(null, create(null, null, null))
}
