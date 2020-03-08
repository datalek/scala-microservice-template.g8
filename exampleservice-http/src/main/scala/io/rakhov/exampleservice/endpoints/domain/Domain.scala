package io.rakhov.exampleservice.endpoints.domain

/**
 * Represents an error on a field.
 *
 * @param fieldName The name of the field.
 * @param fieldLocation The location of the field.
 * @param errorType The type of the error.
 */
final case class FieldError(
  fieldName: String,
  fieldLocation: Location,
  errorType: ErrorType
)

object FieldError {
  def asReadableMsg(fieldError: FieldError): String = {
    val errorType =
      ErrorType.encode(fieldError.errorType)
    val fieldName =
      fieldError.fieldName
    val fieldLocation =
      fieldError.fieldLocation
    s"$errorType '$fieldName' field in $fieldLocation"
  }
}

sealed trait Location

object Location {
  case object Body extends Location
  case object Query extends Location
  case object Header extends Location
  case object Path extends Location
}

sealed trait ErrorType

object ErrorType {
  final case object MissingValue extends ErrorType
  final case class InvalidValue(error: String) extends ErrorType
  final case object Unknown extends ErrorType

  def encode(error: ErrorType): String = {
    error match {
      case MissingValue => "MissingValue"
      case e: InvalidValue => s"InvalidValue:${e.error}"
      case Unknown => ""
    }
  }
  def decode(in: String): Option[ErrorType] = {
    in match {
      case "MissingValue" => Some(MissingValue)
      case error if in.startsWith("InvalidValue:") => Some(InvalidValue(error.replace("InvalidValue:", "")))
      case other => None
    }
  }
  def fromCirceMessage(in: String): Option[ErrorType] = {
    in match {
      case "Attempt to decode value on failed cursor" => Some(ErrorType.MissingValue)
      case "Boolean" => Some(ErrorType.InvalidValue("Not a boolean"))
      case "Char" => Some(ErrorType.InvalidValue("Not a char"))
      case "JsonNumber" => Some(ErrorType.InvalidValue("Not a number"))
      case "JsonObject" => Some(ErrorType.InvalidValue("Not a json object"))
      case "String" => Some(ErrorType.InvalidValue("Not a string"))
      case _ => None
    }
  }
}
