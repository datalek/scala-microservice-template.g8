package generators

import io.rakhov.exampleservice.api._
import io.rakhov.exampleservice.services.authorizations._
import io.rakhov.exampleservice.services.clients._
import zio.test.magnolia._

trait Generators {

  val snapshotGen =
    DeriveGen[Snapshot]

  val clientDefinitionGen =
    DeriveGen[ClientDefinition]

  val clientDefinitionPostponeOwnerGen =
    DeriveGen[ClientDefinition]
      .map(cd => (owner: Identity) => cd.copy(owner = owner))

}

object Generators extends Generators