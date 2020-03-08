package io.rakhov.exampleservice.api.impl

import io.rakhov.exampleservice.api._
import io.rakhov.exampleservice.services.events._
import io.rakhov.exampleservice.services.clients._
import io.rakhov.exampleservice.services.authorizations._
import zio._

class ClientApiImpl(
  authorizationService: Authorizations.Service,
  clientService: Clients.Service
) extends ClientApi.Service {

  private def adapt(clientId: ClientId, input: Option[Client]) = {
    input match {
      case Some(client) =>
        Right(client)
      case None =>
        Left(ClientApiError.NotFound(clientId))
    }
  }
  private def authorize(
    authorizationService: Authorizations.Service,
    authorization: Authorization
  ): UIO[Either[ClientApiError, Identity]] = {
    for {
      either <- authorizationService.authorize(authorization)
      result = either match {
        case Right(identity) =>
          Right(identity)
        case Left(AuthorizationError.Expired(authorization)) =>
          Left(ClientApiError.Forbidden(authorization))
      }
    } yield result
  }

  override def create(
    eventService: Events.Service,
    authorization: Authorization.Bearer,
    definition: ClientDefinition.PostponeOwnerProvisioning
  ): UIO[Either[ClientApiError, (Client, Event)]] = {
    val response = for {
      identity <- authorize(authorizationService, authorization).absolve
      definitionComplete = definition(identity)
      client <- clientService.create(definitionComplete)
      eventDefinition = EventDefinition(
        subject = identity,
        action = Action.ClientCreated(
          client = client.id,
          date = java.time.Instant.now()
        )
      )
      event <- eventService.insert(eventDefinition)
    } yield (client, event)

    response.either
  }

  override def update(
    eventService: Events.Service,
    authorization: Authorization.Bearer,
    clientId: ClientId,
    update: ClientUpdate
  ): UIO[Either[ClientApiError, (Client, Event)]] = {
    val response = for {
      identity <- authorize(authorizationService, authorization).absolve
      option <- clientService.update(clientId, update)
      client <- ZIO.fromEither(adapt(clientId, option))
      eventDefinition = EventDefinition(
        subject = identity,
        action = Action.ClientUpdated(
          client = client.id,
          date = java.time.Instant.now()
        )
      )
      event <- eventService.insert(eventDefinition)
    } yield (client, event)

    response.either
  }

  override def detail(
    authorization: Authorization.Bearer,
    clientId: ClientId
  ): UIO[Either[ClientApiError, Client]] = {
    val response = for {
      identity <- authorize(authorizationService, authorization).absolve
      option <- clientService.detail(clientId)
      result <- ZIO.fromEither(adapt(clientId, option))
    } yield result

    response.either
  }
}
