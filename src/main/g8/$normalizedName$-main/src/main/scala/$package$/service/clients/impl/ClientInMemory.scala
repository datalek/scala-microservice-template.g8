package $package$.service.clients.impl

import cats.data._
import cats.effect._
import $package$.endpoints.models._
import $package$.service.clients._

private[clients] class ClientInMemory extends ClientModule.Service {
  private class Crud(var inMemoryDb: Map[(TenantId, ClientId), Client] = Map()) {
    def insert(tenantId: TenantId, client: Client): IO[Client] = {
      val key = (tenantId, client.id)
      synchronized({ inMemoryDb = inMemoryDb + ((key, client)) })
      IO.pure(client)
    }
    def find(tenantId: TenantId, clientId: ClientId): IO[Option[Client]] = {
      IO(inMemoryDb.get(tenantId, clientId))
    }
    def update(tenantId: TenantId, clientId: ClientId, update: ClientUpdate): IO[Option[Client]] = {
      val key = (tenantId, clientId)
      val result = for {
        element <- OptionT(find(tenantId, clientId))
        updated = element.copy(name = update.name, scope = update.scope)
        _ = synchronized({ inMemoryDb = inMemoryDb.updated(key, updated) })
      } yield updated
      result.value
    }
  }

  private val database =
    new Crud()

  def createClient(
    tenantId: TenantId,
    userId: UserId,
    clientDefinition: ClientDefinition
  ): IO[Client] = {
    val client = Client(
      id = ClientId(scala.util.Random.nextString(10)),
      name = clientDefinition.name,
      owner = Owner(userId),
      scope = clientDefinition.scope)
    database.insert(tenantId, client)
  }

  def retrieveClient(
    tenantId: TenantId,
    clientId: ClientId
  ): IO[Option[Client]] = {
    database.find(tenantId, clientId)
  }

  def updateClient(
    tenantId: TenantId,
    clientId: ClientId,
    clientUpdate: ClientUpdate
  ): IO[Option[Client]] = {
    database.update(tenantId, clientId, clientUpdate)
  }

}
