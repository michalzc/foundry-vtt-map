package michalzc.foundry.maap.document

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSGlobal, JSName}

@js.native
@JSGlobal
class MAapActorCreateData(
    val hp: Int,
    @JSName("def")
    val defence: Int,
    val description: String
) extends js.Object
