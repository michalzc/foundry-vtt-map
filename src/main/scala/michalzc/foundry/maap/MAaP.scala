package michalzc.foundry.maap

import foundryvtt.Hooks
import michalzc.foundry.util.logger

import scala.scalajs.js

object MAaP extends App {

  def init: js.Function = { () =>
    logger.info("Initializing!")

    logger.info("Initialized!")
  }


  Hooks.once("init", init)
}
