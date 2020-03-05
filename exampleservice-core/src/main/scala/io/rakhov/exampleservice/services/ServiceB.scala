package io.rakhov.exampleservice.services

case class ErrorOfB(
  message: String
)

trait ServiceB {
  def doStuff(in: String): Either[ErrorOfB, String]
}