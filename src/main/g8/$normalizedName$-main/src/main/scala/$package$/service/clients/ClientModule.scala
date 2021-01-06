package $package$.service.clients

import cats.effect._
import $package$.endpoints.models._
import $package$.service.clients.impl._

object ClientModule {

  trait Service {
    def createClient(
      tenantId: TenantId,
      userId: UserId,
      clientDefinition: ClientDefinition
    ): IO[Client] // cats.data.ReaderT[IO, E, Client]

    def retrieveClient(
      tenantId: TenantId,
      clientId: ClientId
    ): IO[Option[Client]] // cats.data.ReaderT[IO, E, Option[Client]]

    def updateClient(
      tenantId: TenantId,
      clientId: ClientId,
      clientUpdate: ClientUpdate
    ): IO[Option[Client]] // cats.data.ReaderT[IO, E, Option[Client]]
  }

  def Live(): IO[ClientModule.Service] =
    IO(new ClientInMemory())

}
