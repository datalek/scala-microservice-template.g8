package $package$.services

case class ErrorOfB(
  message: String
)

trait ServiceB {
  def doStuff(in: String): Either[ErrorOfB, String]
}