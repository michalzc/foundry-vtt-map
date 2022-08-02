package michalzc.foundry.maap.document

import scala.scalajs.js.annotation.{JSGlobal, JSName}
import scalajs.js

@JSGlobal
@js.native
class MAapPCActorCreateData(
    override val hp: Int,
    @JSName("def")
    override val defence: Int,
    override val description: String,
) extends MAapActorCreateData(hp, defence, description)
