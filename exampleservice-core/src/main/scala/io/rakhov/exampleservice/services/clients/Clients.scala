package io.rakhov.exampleservice.services.clients

import zio._

object Clients {
  trait Service {
    def create(
      definition: ClientDefinition
    ): UIO[Client]
    
    def update(
      id: ClientId,
      update: ClientUpdate
    ): UIO[Option[Client]]
    
    def detail(
      id: ClientId
    ): UIO[Option[Client]]
  }
}
