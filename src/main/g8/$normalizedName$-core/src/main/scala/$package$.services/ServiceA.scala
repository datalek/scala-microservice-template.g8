package $package$.services

case class InputOfA(
  key0: String
)

case class OutputOfA(
  field0: Double
)

case class ErrorOfA(
  message: String
)

trait ServiceA {
  def doStuff(in: InputOfA): Either[ErrorOfA, OutputOfA]
}