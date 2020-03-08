package io.rakhov.exampleservice.api.impl

import io.rakhov.exampleservice.api._
import io.rakhov.exampleservice.services.authorizations._
import io.rakhov.exampleservice.services.authorizations.impl._
import io.rakhov.exampleservice.services.clients._
import io.rakhov.exampleservice.services.clients.impl._
import zio.test._

object ClientApiImplSpec extends DefaultRunnableSpec {
  override def spec =
    ClientApiSpec.suites(
      apiGen = ClientApiStub.generate
    )
}

object ClientApiStub {
  def generate(snapshot: Snapshot): ClientApi.Service = {

    val authorizationService: Authorizations.Service =
      new AuthorizationsImpl(
        backingStore = Map(snapshot.authorizationAndIdentity)
      )
    val clientService: Clients.Service =
      new ClientsStub()

    new ClientApiImpl(
      authorizationService = authorizationService,
      clientService = clientService
    )
  }
}