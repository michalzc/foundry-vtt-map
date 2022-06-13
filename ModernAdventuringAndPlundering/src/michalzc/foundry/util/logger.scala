package michalzc.foundry.util

import michalzc.foundry.maap.MaapConf
import org.scalajs.dom.console

object logger {

  def info(msg: String) = {
    console.log(s"${MaapConf.systemName} | $msg")
  }

}
