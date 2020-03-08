package io.rakhov.exampleservice.endpoints.models

final case class ClientDefinition(
  name: String,
  scope: Seq[String]
)
