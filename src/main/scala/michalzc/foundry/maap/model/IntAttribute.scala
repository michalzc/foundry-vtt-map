package michalzc.foundry.maap.model

import scalajs.js

@js.native
trait IntAttribute extends js.Object {
  def min: Int = js.native
  def max: Int = js.native
  def value: Int = js.native
}
