package michalzc.foundry.maap.document

import foundryvtt.{DocumentModificationContext, Item}
import foundryvtt.data.ItemData

import scala.scalajs.js
import scala.scalajs.js.annotation.JSExportTopLevel

@JSExportTopLevel("MaapItem")
class MaapItem(data: ItemData[MaapItemData], context: js.UndefOr[DocumentModificationContext]) extends Item(data, context){

}
