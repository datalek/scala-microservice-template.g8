package io.rakhov.exampleservice.api

import generators._
import io.rakhov.exampleservice.service.events.EventServiceStub
import io.rakhov.exampleservice.services.events._
import io.rakhov.exampleservice.services.clients._
import io.rakhov.exampleservice.services.authorizations._
import zio._
import zio.test._
import zio.test.Assertion._

case class Snapshot(
  authorizationAndIdentity: (Authorization.Bearer, Identity)
)

object ClientApiSpec {

  private def createSuite(apiGen: Snapshot => ClientApi.Service) = {
    suite("the 'create' method")(

      testM("create a client without error")(
        for {
          snapshot <- Generators.snapshotGen.sample.runHead.map(_.get.value)
          definition <- Generators.clientDefinitionPostponeOwnerGen.sample.runHead.map(_.get.value)
          eventService = EventServiceStub()
          (authorization, identity) = snapshot.authorizationAndIdentity
          api = apiGen(snapshot)

          //`.absolve` instead of `.map(_.right.get)` doesn't work
          pair <- api.create(eventService, authorization, definition).absolve
          (actualClient, actualEvent) = pair
          actualEvents <- eventService.events

          expected = Client(
            id = actualClient.id,
            name = definition(identity).name,
            owner = identity,
            scope = definition(identity).scope
          )
          assertion0 = assert(actualClient)(equalTo(expected))

          expectedEvents = List(Event(
            id = actualEvent.id,
            subject = identity,
            action = Action.ClientCreated(
              expected.id,
              date = actualEvent.action.date
            )
          ))
          assertion1 = assert(actualEvents)(equalTo(expectedEvents))
        } yield assertion0 && assertion1
      ),

      testM("reply with Unauthorized if authorization is expired")(
        for {
          snapshot <- Generators.snapshotGen.sample.runHead.map(_.get.value)
          definition <- Generators.clientDefinitionPostponeOwnerGen.sample.runHead.map(_.get.value)
          eventService = EventServiceStub()
          (authorization, identity) = snapshot.authorizationAndIdentity
          wrongAuthorization = Authorization.Bearer(authorization.value + "-wrong")
          api = apiGen(snapshot)

          actual <- api.create(eventService, wrongAuthorization, definition)

          expected = Left(ClientApiError.Forbidden(wrongAuthorization))
          assertion = assert(actual)(equalTo(expected))

          actualEvents <- eventService.events
          expectedEvents = Nil
          assertion1 = assert(actualEvents)(equalTo(expectedEvents))
        } yield assertion && assertion1
      )
    )
  }

  def suites(apiGen: Snapshot => ClientApi.Service) = {
    suite("The ClientApi")(
      createSuite(apiGen)
    )
  }

}
