package com.karasiq.videojs

import scala.scalajs.js

object VjsUtils {
  def ready(f: VideoJSPlayer ⇒ Unit): js.Function = {
    js.ThisFunction.fromFunction1((p: VideoJSPlayer) ⇒ f(p))
  }
}
