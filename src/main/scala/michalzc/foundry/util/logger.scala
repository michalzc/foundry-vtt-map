package michalzc.foundry.util

import michalzc.foundry.maap.MaapConf
import org.scalajs.dom.console

import scala.scalajs.js

object logger {

  def info(msg: String, args: js.Any*) = {
    console.log(s"${MaapConf.systemName} | $msg", args: _*)
  }

}
