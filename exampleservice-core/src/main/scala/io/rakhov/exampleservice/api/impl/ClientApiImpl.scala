package io.rakhov.exampleservice.api.impl

import io.rakhov.exampleservice.api._
import io.rakhov.exampleservice.services.events._
import io.rakhov.exampleservice.services.clients._
import io.rakhov.exampleservice.services.authorizations._
import zio.UIO

class ClientApiImpl(
  authorizationService: Authorizations.Service,
  clientService: Clients.Service
) extends ClientApi.Service {
  
  override def create(
    eventService: Events.Service,
    authorization: Authorization.Bearer,
    definition: ClientDefinition
  ): UIO[Either[ClientApiError, Client]] = {
    ???
  }
  
  override def update(
    eventService: Events.Service,
    authorization: Authorization.Bearer,
    clientId: ClientId,
    definition: ClientUpdate
  ): UIO[Either[ClientApiError, Client]] = {
    ???
  }
  
  override def detail(
    authorization: Authorization.Bearer,
    definition: ClientId
  ): UIO[Either[ClientApiError, Client]] = {
    ???
  }
}
