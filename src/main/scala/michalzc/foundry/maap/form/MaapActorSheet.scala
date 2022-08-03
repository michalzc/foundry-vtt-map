package michalzc.foundry.maap.form

import foundryvtt.forms.{ActorSheet, SheetContext}
import foundryvtt.global.Global
import michalzc.foundry.maap.document.{MaapActor, MaapActorData}
import michalzc.foundry.util.logger

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.annotation.JSExportStatic

class MaapActorSheet(actor: MaapActor, options: js.Object) extends ActorSheet[MaapActorData, MaapActor](actor, options) {
  override def getData(): js.Object = {
    val data = super.getData().asInstanceOf[SheetContext[MaapActorData, MaapActor]]
    logger.info("Sheet data", data)
    logger.info("Actor", data.actor)
    logger.info("Data", data.data)

    new MaapActorSheetContext(data.actor, data.data)
  }
}

object MaapActorSheet {

  @JSExportStatic
  def defaultOptions: js.Any = Global.mergeObject(
    ActorSheet.defaultOptions,
    MaapActorSheetOptions(
      width = 800,
      height = 600,
      template = Some("systems/maap/html/playerSheet.html"),
      tabs = Map("navSelector" -> ".sheet-tabs", "contentSelector" -> ".sheet-body", "initial" -> "features"),
      classes = List("maap", "sheet", "actor")
    ),
  )
}

class MaapActorSheetOptions(
    val classes: js.Array[String],
    val template: js.UndefOr[String],
    val width: Int,
    val height: Int,
    val tabs: js.Array[js.Dictionary[String]],
) extends js.Object

object MaapActorSheetOptions {
  def apply(
      width: Int,
      height: Int,
      classes: List[String] = List.empty,
      template: Option[String] = None,
      tabs: Map[String, String] = Map.empty,
  ): MaapActorSheetOptions = new MaapActorSheetOptions(
    classes.toJSArray,
    template.orUndefined,
    width,
    height,
    js.Array(tabs.toJSDictionary),
  )
}
