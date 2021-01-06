package $package$.endpoints.models

final case class ClientUpdate(
  name: String,
  scope: Seq[String]
)
