package michalzc.foundry.maap

import foundryvtt.Hooks
import foundryvtt.global.condig.CONFIG
import michalzc.foundry.maap.document.{MaapActor, MaapItem}
import michalzc.foundry.util.logger

import scala.scalajs.js

object MAaP extends App {

  def init: js.Function = { () =>
    logger.info("Initializing!")
    CONFIG.Actor.entityClass = js.constructorOf[MaapActor]
    CONFIG.Item.entityClass = js.constructorOf[MaapItem]
    logger.info("Initialized!")
  }


  Hooks.once("init", init)
}
