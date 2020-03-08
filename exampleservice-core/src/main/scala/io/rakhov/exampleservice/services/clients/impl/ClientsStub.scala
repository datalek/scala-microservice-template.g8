package io.rakhov.exampleservice.services.clients.impl

import io.rakhov.exampleservice.services.clients._
import zio._

class ClientsStub extends Clients.Service {

  private var internalClients: List[Client] = Nil

  def create(definition: ClientDefinition): UIO[Client] = synchronized {
    val client = Client(
      id = ClientId(internalClients.length.toString),
      name = definition.name,
      owner = definition.owner,
      scope = definition.scope
    )
    internalClients = client :: internalClients
    ZIO.succeed(client)
  }

  def update(id: ClientId, update: ClientUpdate): UIO[Option[Client]] = synchronized {
    for {
      eventually <- detail(id)
      result = eventually match {
        case Some(client) =>
          val clientUpdated = client.copy(
            name = update.name,
            scope = update.scope
          )
          internalClients = client :: internalClients.filter(_.id != id)
          Option(clientUpdated)
        case None =>
          None
      }
    } yield result
  }

  def detail(id: ClientId): UIO[Option[Client]] = {
    ZIO.succeed(internalClients.find(_.id == id))
  }
}

//private[events] class EventServiceStub extends Events.Service {
//
//  private var internalEvents: List[Event] = Nil
//
//  def insert(definition: EventDefinition): UIO[Event] = synchronized {
//    val event = Event(
//      id = EventId(internalEvents.length.toString),
//      subject = definition.subject,
//      action = definition.action
//    )
//    internalEvents = event :: internalEvents
//    ZIO.succeed(event)
//  }
//
//  def events: UIO[List[Event]] =
//    ZIO.succeed(internalEvents)
//
//}
//
//object EventServiceStub {
//  def apply(): EventServiceStub =
//    new EventServiceStub()
//}
//
