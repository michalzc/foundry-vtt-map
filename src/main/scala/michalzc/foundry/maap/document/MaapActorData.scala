package michalzc.foundry.maap.document

import michalzc.foundry.maap.model.IntAttribute

import scala.scalajs.js.annotation.JSName
import scalajs.js

@js.native
trait MaapActorData extends js.Object {
  def hp: IntAttribute = js.native

  @JSName("def")
  def defence: Int = js.native

  def description: String = js.native
}
