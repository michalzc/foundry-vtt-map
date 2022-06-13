package michalzc.foundry.maap

import foundryvtt.Hooks

import scala.scalajs.js

object MAaP extends App {

  def init: js.Function = { () =>

  }


  Hooks.once("init", init)
}
