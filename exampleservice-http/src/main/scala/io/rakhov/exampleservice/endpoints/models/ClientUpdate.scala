package io.rakhov.exampleservice.endpoints.models

final case class ClientUpdate(
  name: String,
  scope: Seq[String]
)
