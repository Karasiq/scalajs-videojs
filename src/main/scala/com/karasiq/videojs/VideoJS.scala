package com.karasiq.videojs

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName
import scala.scalajs.js.|

@js.native
@JSName("videojs")
object VideoJS extends js.Object {
  def apply(element: String | js.Object, settings: js.Object = new VideoJSOptions, callback: js.Function = () ⇒ ()): Unit = js.native
}
