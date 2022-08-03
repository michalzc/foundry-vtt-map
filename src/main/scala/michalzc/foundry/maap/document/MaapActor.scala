package michalzc.foundry.maap.document

import foundryvtt.{Actor, DocumentModificationContext}
import foundryvtt.data.ActorData

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("MaapActor")
class MaapActor(data: ActorData[MapActorData], context: js.UndefOr[DocumentModificationContext]) extends Actor(data, context){

}
