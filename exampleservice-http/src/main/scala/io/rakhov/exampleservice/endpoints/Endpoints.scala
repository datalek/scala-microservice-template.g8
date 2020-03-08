package io.rakhov.exampleservice.endpoints

import cats._
import cats.data._
import cats.effect._
import io.circe.generic.auto._
import io.circe.syntax._
import io.rakhov.exampleservice.endpoints.domain._
import io.rakhov.exampleservice.endpoints.models._
import org.http4s._
import org.http4s.circe._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.util._

import scala.language.higherKinds

sealed trait Response

sealed trait CreateClientResponse
  extends Response

sealed trait UpdateClientResponse
  extends Response

sealed trait RetrieveClientResponse
  extends Response

object Responses {

  final case class Ok(body: Client)
    extends CreateClientResponse
    with UpdateClientResponse
    with RetrieveClientResponse

  final case class BadRequest(body: Error)
    extends CreateClientResponse
    with UpdateClientResponse
    with RetrieveClientResponse

  final case class Unauthorized(body: Error)
    extends CreateClientResponse
    with UpdateClientResponse
    with RetrieveClientResponse

  final case class Forbidden(body: Error)
    extends CreateClientResponse
    with UpdateClientResponse
    with RetrieveClientResponse

  final case class NotFound()
    extends UpdateClientResponse
    with RetrieveClientResponse

  private val challenge =
    Challenge(scheme = "Bearer", realm = "Please enter a valid API key")
  val toHttp4sResponse: Response => IO[org.http4s.Response[IO]] = {
    case Responses.Ok(client)          => org.http4s.dsl.io.Ok(client.asJson)
    case Responses.BadRequest(error)   => org.http4s.dsl.io.BadRequest(error.asJson)
    case Responses.Unauthorized(error) => org.http4s.dsl.io.Unauthorized(challenge)
    case Responses.Forbidden(error)    => org.http4s.dsl.io.Forbidden(error.asJson)
    case Responses.NotFound()          => org.http4s.dsl.io.NotFound()
  }
}

object CreateClientResponse {
  val Ok = Responses.Ok
  val BadRequest = Responses.BadRequest
  val Unauthorized = Responses.Unauthorized
  val Forbidden = Responses.Forbidden
}

object UpdateClientResponse {
  val Ok = Responses.Ok
  val NotFound = Responses.NotFound
  val BadRequest = Responses.BadRequest
  val Unauthorized = Responses.Unauthorized
  val Forbidden = Responses.Forbidden
}

object RetrieveClientResponse {
  val Ok = Responses.Ok
  val NotFound = Responses.NotFound
  val BadRequest = Responses.BadRequest
  val Unauthorized = Responses.Unauthorized
  val Forbidden = Responses.Forbidden
}

final case class CreateClientRequest(
  tenantId: TenantId,
  authorizationToken: AuthorizationToken,
  clientDefinition: ClientDefinition
)

final case class UpdateClientRequest(
  tenantId: TenantId,
  authorizationToken: AuthorizationToken,
  clientId: ClientId,
  update: ClientUpdate
)

final case class RetrieveClientRequest(
  tenantId: TenantId,
  authorizationToken: AuthorizationToken,
  clientId: ClientId
)

case class Endpoints(
  createClient: Either[NonEmptyList[FieldError], CreateClientRequest]
             => IO[CreateClientResponse],
  updateClient: Either[NonEmptyList[FieldError], UpdateClientRequest]
             => IO[UpdateClientResponse],
  retrieveClient: Either[NonEmptyList[FieldError], RetrieveClientRequest]
               => IO[RetrieveClientResponse]
)

object Endpoints {

  type EndpointRequestValidated[A] =
    Validated[NonEmptyList[FieldError], A]

  private def extractFromHeader[M[_]](key: CaseInsensitiveString, request: Request[M]) =
    request.headers.get(key) match {
      case Some(header) => Right(header.value)
      case None         => Left(NonEmptyList.one(FieldError(key.value, Location.Header, ErrorType.MissingValue)))
    }
  private def extractFromJsonBodyAs[T: io.circe.Decoder](request: Request[IO]) =
    request.as(
      IO.ioEffect, jsonOf[IO, T].transform({
        case Left(decodeFailure) =>
          Right(Left(NonEmptyList.one(FieldError("body", Location.Body, ErrorType.InvalidValue("")))))
        case Right(body)         =>
          Right(Right(body))
      })
    )

  private def extractTenant[M[_]](request: Request[M]) =
    extractFromHeader(CaseInsensitiveString("x-tenant-id"), request)
      .map(TenantId)
  private def extractAuthorizationToken[M[_]](request: Request[M]) =
    extractFromHeader(CaseInsensitiveString("authorization"), request)
      .map(AuthorizationToken)

  def endpoints(endpoints: Endpoints) =
    HttpRoutes.of[IO]({
      case request @ POST -> Root / "clients" =>
        for {
          clientDefinition <- extractFromJsonBodyAs[ClientDefinition](request)
          validatedRequest = Applicative[EndpointRequestValidated].map3(
            Validated.fromEither(extractTenant(request)),
            Validated.fromEither(extractAuthorizationToken(request)),
            Validated.fromEither(clientDefinition)
            )(CreateClientRequest).toEither
          result <- endpoints.createClient(validatedRequest)
          response <- Responses.toHttp4sResponse(result)
        } yield response
      case request @ POST -> Root / "clients" / clientId =>
        for {
          updateDefinition <- extractFromJsonBodyAs[ClientUpdate](request)
          validatedRequest = Applicative[EndpointRequestValidated].map3(
            Validated.fromEither(extractTenant(request)),
            Validated.fromEither(extractAuthorizationToken(request)),
            Validated.fromEither(updateDefinition)
          )(UpdateClientRequest(_, _, ClientId(clientId), _)).toEither
          result <- endpoints.updateClient(validatedRequest)
          response <- Responses.toHttp4sResponse(result)
        } yield response
      case request @ GET -> Root / "clients" / clientId =>
        for {
          validatedRequest <- IO(Applicative[EndpointRequestValidated].map2(
            Validated.fromEither(extractTenant(request)),
            Validated.fromEither(extractAuthorizationToken(request))
          )(RetrieveClientRequest(_, _, ClientId(clientId))).toEither)
          result <- endpoints.retrieveClient(validatedRequest)
          response <- Responses.toHttp4sResponse(result)
        } yield response
    })
    .orNotFound

}
