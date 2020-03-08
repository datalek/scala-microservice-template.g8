package io.rakhov.exampleservice.api

import io.rakhov.exampleservice.services.events._
import io.rakhov.exampleservice.services.clients._
import io.rakhov.exampleservice.services.authorizations._
import zio._

object ClientApi {
  trait Service {

    def create(
      eventService: Events.Service,
      authorization: Authorization.Bearer,
      definition: ClientDefinition.PostponeOwnerProvisioning
    ): UIO[Either[ClientApiError, (Client, Event)]]

    def update(
      eventService: Events.Service,
      authorization: Authorization.Bearer,
      clientId: ClientId,
      update: ClientUpdate
    ): UIO[Either[ClientApiError, (Client, Event)]]

    def detail(
      authorization: Authorization.Bearer,
      clientId: ClientId
    ): UIO[Either[ClientApiError, Client]]

  }
}