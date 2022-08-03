package michalzc.foundry.maap.document

import foundryvtt.{Actor, DocumentModificationContext}
import foundryvtt.data.ActorData

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSExportStatic, JSExportTopLevel}

@JSExportTopLevel("MaapActor")
class MaapActor(data: ActorData[MaapActorData], context: js.UndefOr[DocumentModificationContext]) extends Actor(data, context){

}

object MaapActor {
  @JSExportStatic
  def name: String = "MaapActor"
}