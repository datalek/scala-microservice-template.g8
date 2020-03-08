package io.rakhov.exampleservice.http

import akka.http.scaladsl.model._
import akka.http.scaladsl.server._
import akka.http.scaladsl.server.Directives._
import io.rakhov.exampleservice.api._
import io.rakhov.exampleservice.services.clients._
import io.rakhov.exampleservice.services.authorizations._
import io.rakhov.exampleservice.services.events._
import zio.UIO

object ClientApiRoutes {

  import JsonHandler._

  def route(api: ClientApi.Service, eventService: Events.Service): Route = {
    pathPrefix("clients") {
      headerValueByName("x-tenant-id").as(TenantId) { tenantId =>
        headerValueByName("authorization").as(Authorization.Bearer) { authorization =>
          concat(
            post {
              entity(as[TenantId => ClientDefinition.PostponeOwnerProvisioning]) { partialClientDefinition =>
                val clientDefinition = partialClientDefinition(tenantId)
                val result = api.create(
                  eventService = eventService,
                  authorization = authorization,
                  definition = clientDefinition
                )
                adaptResponse2(result)
              }
            },
            put {
              pathPrefix(Segment).as(ClientId.apply) { clientId =>
                entity(as[ClientUpdate]) { clientUpdate =>
                  val result = api.update(
                    eventService = eventService,
                    authorization = authorization,
                    clientId = clientId,
                    update = clientUpdate
                  )
                  adaptResponse2(result)
                }
              }
            },
            get {
              pathPrefix(Segment).as(ClientId.apply) { clientId =>
                val result = api.detail(authorization, clientId)
                adaptResponse(result)
              }
            }
          )
        }
      }
    }
  }

  private def adaptResponse2(in: UIO[Either[ClientApiError, (Client, Event)]]) =
    adaptResponse(in.map(_.map({ case (client, event) => client })))

  private def adaptResponse(in: UIO[Either[ClientApiError, Client]]) = {
    onSuccess(zio.Runtime.default.unsafeRunToFuture(in))({
      case Left(ClientApiError.Forbidden(authorization)) =>
        complete(StatusCodes.Forbidden, ErrorResponse(code = 4030, message = Option("Forbidden"), errors = None))
      case Left(ClientApiError.Unauthorized(authorization)) =>
        complete(StatusCodes.Unauthorized, ErrorResponse(code = 4010, message = Option("Unauthorized"), errors = None))
      case Left(ClientApiError.NotFound(clientId)) =>
        complete(StatusCodes.NotFound, ErrorResponse(code = 4040, message = Option("Not Found"), errors = None))
      case Right(result) =>
        complete(StatusCodes.OK, result)
    })
  }

}
