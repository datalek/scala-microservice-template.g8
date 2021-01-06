package $package$.endpoints.models

final case class Client(
  id: ClientId,
  name: String,
  owner: Owner,
  scope: Seq[String]
)
