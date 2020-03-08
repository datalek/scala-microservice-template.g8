package io.rakhov.exampleservice.services.authorizations.impl

import io.rakhov.exampleservice.services.authorizations._
import zio._

object AuthorizationsImplConf {
  def load(conf: com.typesafe.config.Config): IO[Throwable, Map[Authorization, Identity]] = {
    import scala.util._
    import scala.collection.JavaConverters._
    def retrieveFromConf() =
      ZIO.fromTry(Try(conf.getConfigList("authorizations.token-identity-correlation")).map(_.asScala))
    val result = for {
      tokenIdentityList <- retrieveFromConf()
      result = tokenIdentityList.map(element => {
        val auth = element.getString("token")
        val identity = element.getString("identity")
        (Authorization.Bearer(auth): Authorization) -> (Identity.User(UserId(identity)): Identity)
      })
    } yield result.toMap
    result
  }
}

class AuthorizationsImpl(
  backingStore: Map[Authorization, Identity]
) extends Authorizations.Service {

  def authorize(authorization: Authorization): UIO[Either[AuthorizationError.Expired, Identity]] = {
    val result = backingStore.get(authorization) match {
      case Some(identity) =>
        Right(identity)
      case None =>
        Left(AuthorizationError.Expired(authorization))
    }
    ZIO.succeed(result)
  }

}
