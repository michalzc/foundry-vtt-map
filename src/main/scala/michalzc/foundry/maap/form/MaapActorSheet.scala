package michalzc.foundry.maap.form

import foundryvtt.forms.ActorSheet
import michalzc.foundry.maap.document.MaapActor

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportStatic

class MaapActorSheet(actor: MaapActor, options: js.Object) extends ActorSheet(actor, options){

}


object MaapActorSheet {

  @JSExportStatic
  def defaultOptions: js.Object = ActorSheet.defaultOptions
}