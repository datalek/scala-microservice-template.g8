package io.rakhov.exampleservice.endpoints.models

final case class Error(
  code: Int,
  message: String,
  errors: List[Error] = Nil
)
