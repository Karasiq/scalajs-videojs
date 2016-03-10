package com.karasiq.videojs

import scala.scalajs.js

object VjsUtils {
  def ready(f: Player ⇒ Unit): js.Function = {
    js.ThisFunction.fromFunction1((p: Player) ⇒ f(p))
  }
}
