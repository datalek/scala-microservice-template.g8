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
      definition: ClientDefinition
    ): UIO[Either[ClientApiError, Client]]
    
    def update(
      eventService: Events.Service,
      authorization: Authorization.Bearer,
      clientId: ClientId,
      definition: ClientUpdate
    ): UIO[Either[ClientApiError, Client]]
  
    def detail(
      authorization: Authorization.Bearer,
      definition: ClientId
    ): UIO[Either[ClientApiError, Client]]
    
  }
}
