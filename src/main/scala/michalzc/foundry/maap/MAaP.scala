package michalzc.foundry.maap

import foundryvtt.Hooks
import foundryvtt.collection.{Actors, SheetOptions}
import foundryvtt.forms.ActorSheet
import foundryvtt.global.config.CONFIG
import michalzc.foundry.maap.document.{MaapActor, MaapItem}
import michalzc.foundry.maap.form.MaapActorSheet
import michalzc.foundry.util.logger

import scala.scalajs.js
import scala.scalajs.js.JSConverters._

object MAaP extends App {

  def init: js.Function = { () =>
    logger.info("Initializing!")
    CONFIG.Actor.documentClass = js.constructorOf[MaapActor]
    CONFIG.Item.documentClass = js.constructorOf[MaapItem]

    Actors.unregisterSheet("core", js.constructorOf[ActorSheet[Any]])
//    Actors.registerSheet("maap", js.constructorOf[Maap])
    Actors.registerSheet("maap", js.constructorOf[MaapActorSheet], buildSheetOpts("MA&P Sheet", "pc"))

    logger.info("Initialized!")
  }

  def buildSheetOpts(label: String, tags: String*): SheetOptions =
    new SheetOptions(label, tags.toJSArray, true)


  Hooks.once("init", init)
}
