package $package$.service

import cats.data._
import cats.effect._
import $package$.endpoints._
import $package$.endpoints.domain._
import $package$.endpoints.models._
import $package$.service.authorization._
import $package$.service.clients._
import $package$.service.configurations.Config
import $package$.service.events._

object ClientEndpointsModule {

  private def asError(
    errors: NonEmptyList[FieldError]
  ): Error = {
    val error =
      Error(code = 400, message = "", errors = Nil)
    errors.foldLeft(error)( (error, fieldError) => {
      val errors = error.errors
      fieldError match {
        case FieldError("authorization", _, ErrorType.MissingValue) =>
          val errorToAdd =
            Error(401, FieldError.asReadableMsg(fieldError))
          error.copy(code = 401, errors = errorToAdd :: errors)
        case _ =>
          val errorToAdd =
            Error(400, FieldError.asReadableMsg(fieldError))
          error.copy(errors = errorToAdd :: errors)
      }
    })
  }

  private val notFoundResponse: Responses.NotFound =
    Responses.NotFound()

  private def authenticate(
    authenticateService: AuthorizationModule.Service,
    authenticateToken: AuthorizationToken
  ): IO[Either[Responses.Unauthorized, UserId]] = {
    authenticateService.authenticate(authenticateToken).map(_.fold(
      error => Left(Responses.Unauthorized(Error(401, "errors", Nil))),
      Right(_))
    )
  }

  object Pure {
    def createClient(
      clientService: ClientModule.Service,
      authenticateService: AuthorizationModule.Service,
      eventService: EventModule.Service,
      validatedRequest: Either[NonEmptyList[FieldError], CreateClientRequest]
    ): IO[CreateClientResponse] = {
      val input = validatedRequest.fold(
        errors => Left(CreateClientResponse.BadRequest(asError(errors))),
        request => Right(CreateClientRequest.unapply(request).get)
      )

      val execute = for {
        request <- EitherT(IO(input))
        (tenantId, authorizationToken, clientDefinition) = request
        userId <- EitherT(authenticate(authenticateService, authorizationToken))

        // https://github.com/typelevel/cats/issues/3275
        insertResult <- EitherT.right[CreateClientResponse](
          clientService.createClient(tenantId, userId, clientDefinition)
        )
        _ <- EitherT.right[CreateClientResponse](
          eventService.sendEvent(Event.CreateEvent(insertResult, java.time.Instant.now()))
        )
      } yield CreateClientResponse.Ok(insertResult)

      execute.fold(identity, identity)
    }

    def updateClient(
      clientService: ClientModule.Service,
      authenticateService: AuthorizationModule.Service,
      eventService: EventModule.Service,
      validatedRequest: Either[NonEmptyList[FieldError], UpdateClientRequest]
    ): IO[UpdateClientResponse] = {
      val input = validatedRequest.fold(
        errors => Left(UpdateClientResponse.BadRequest(asError(errors))),
        request => Right(UpdateClientRequest.unapply(request).get)
      )

      val execute = for {
        request <- EitherT(IO(input))
        (tenantId, authorizationToken, clientIdIn, clientUpdate) = request
        _ <- EitherT(authenticate(authenticateService, authorizationToken))
        insertResult <- EitherT(
          clientService.updateClient(tenantId, clientIdIn, clientUpdate)
            .map(_.toRight[UpdateClientResponse](notFoundResponse))
        )
        _ <- EitherT.right[UpdateClientResponse](
          eventService.sendEvent(Event.CreateEvent(insertResult, java.time.Instant.now()))
        )
      } yield UpdateClientResponse.Ok(insertResult)

      execute.fold(identity, identity)
    }

    def retrieveClient(
      clientService: ClientModule.Service,
      authenticateService: AuthorizationModule.Service,
      validatedRequest: Either[NonEmptyList[FieldError], RetrieveClientRequest]
    ): IO[RetrieveClientResponse] = {
      val input = validatedRequest.fold(
        errors => Left(RetrieveClientResponse.BadRequest(asError(errors))),
        request => Right(RetrieveClientRequest.unapply(request).get)
      )

      val execute = for {
        request <- EitherT(IO(input))
        (tenantId, authorizationToken, clientIdIn) = request
        _ <- EitherT(authenticate(authenticateService, authorizationToken))
        insertResult <- EitherT(
          clientService.retrieveClient(tenantId, clientIdIn)
            .map(_.toRight[RetrieveClientResponse](notFoundResponse))
        )
      } yield CreateClientResponse.Ok(insertResult)

      execute.fold(identity, identity)
    }
  }

  def endpoints(
    clientService: ClientModule.Service,
    authorizationService: AuthorizationModule.Service,
    eventService: EventModule.Service,
    config: Config
  ): IO[Endpoints] = {
    val endpoints = Endpoints(
      createClient = ClientEndpointsModule.Pure.createClient(clientService, authorizationService, eventService, _),
      updateClient = ClientEndpointsModule.Pure.updateClient(clientService, authorizationService, eventService, _),
      retrieveClient = ClientEndpointsModule.Pure.retrieveClient(clientService, authorizationService, _)
    )
    IO.pure(endpoints)
  }
}
