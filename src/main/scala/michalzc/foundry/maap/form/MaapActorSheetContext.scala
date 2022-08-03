package michalzc.foundry.maap.form

import foundryvtt.data.ActorData
import michalzc.foundry.maap.document.{MaapActor, MaapActorData}

import scala.scalajs.js

class MaapActorSheetContext(
    val actor: MaapActor,
    val data: ActorData[MaapActorData],
) extends js.Object
