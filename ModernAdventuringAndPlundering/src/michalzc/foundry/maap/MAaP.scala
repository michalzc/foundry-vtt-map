package michalzc.foundry.maap

import foundryvtt.Hooks

import scala.scalajs.js

object MAaP extends App {

  val systemName = "Modern Adventuring and Plundering System"

  def init: js.Function = { () =>
    scribe.info(s"Initializing $systemName")

    scribe.info(s"$systemName initialized")
  }


  Hooks.once("init", init)
}
