package io.rakhov.exampleservice.http

import de.heikoseeberger.akkahttpcirce._
import io.circe._
import io.rakhov.exampleservice.services.clients._
import io.rakhov.exampleservice.services.authorizations._

trait JsonHandler extends ErrorAccumulatingCirceSupport {

  /* tenantId */
  implicit lazy val tenantIdDecoder: Decoder[TenantId] =
    Decoder.decodeString.map(TenantId)

  /* userId */
  implicit lazy val userIdDecoder: Decoder[UserId] =
    Decoder.decodeString.map(UserId)

  /* clientId */
  implicit lazy val clientIdEncoder: Encoder[ClientId] =
    Encoder.encodeString.contramap(client => client.value)

  /* identity */
  object IdentityKeys {
    val id = "id"
  }
  implicit lazy val identityDecoder: Decoder[Identity] =
    Decoder.forProduct1(
      IdentityKeys.id
    )(Identity.User)
  implicit lazy val identityEncoder: Encoder[Identity] =
    Encoder.forProduct1(
      IdentityKeys.id
    )({
        case Identity.User(UserId(id)) =>
          id
      })

  /* clientDefinition */
  object ClientDefinitionKeys {
    val (tenantIdKey, nameKey, ownerKey, scopeKey) =
      ("tenantId", "name", "owner", "scope")
  }
  implicit lazy val clientDefinitionPostponedOwnerDecoder: Decoder[TenantId => ClientDefinition.PostponeOwnerProvisioning] =
    Decoder.forProduct2(
      ClientDefinitionKeys.nameKey,
      ClientDefinitionKeys.scopeKey
    )((n: String, s: Seq[String]) => (t: TenantId) => (o: Identity) => ClientDefinition(t, n, o, s))

  /* clientUpdate */
  object ClientUpdateKeys {
    val (nameKey, scopeKey) =
      ("name", "scope")
  }
  implicit lazy val clientUpdateDecoder: Decoder[ClientUpdate] =
    Decoder.forProduct2(
      ClientUpdateKeys.nameKey,
      ClientUpdateKeys.scopeKey
    )(ClientUpdate)

  /* client */
  object ClientKeys {
    val (idKey, nameKey, ownerKey, scopeKey) =
      ("id", "name", "owner", "scope")
  }
  implicit lazy val clientDecoder: Encoder[Client] =
    Encoder.forProduct4(
      ClientKeys.idKey,
      ClientKeys.nameKey,
      ClientKeys.ownerKey,
      ClientKeys.scopeKey
    )(client => (client.id, client.name, client.owner, client.scope))

}

object JsonHandler extends JsonHandler
