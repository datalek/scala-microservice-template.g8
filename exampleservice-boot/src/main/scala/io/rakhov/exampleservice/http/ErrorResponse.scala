package io.rakhov.exampleservice.http

import io.circe._

case class ErrorResponse(
  code: Long,
  message: Option[String],
  errors: Option[Seq[ErrorResponse]]
)

object ErrorResponse {
  implicit val clientDefinitionDecoder: Encoder[ErrorResponse] =
    Encoder.forProduct3(
      "code",
      "message",
      "errors"
    )(error => (error.code, error.message, error.errors))
}