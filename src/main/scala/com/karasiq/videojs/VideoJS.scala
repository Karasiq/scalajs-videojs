package com.karasiq.videojs

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.annotation.JSName
import scala.scalajs.js.|

// TODO: http://docs.videojs.com/docs/api/component.html, http://docs.videojs.com/docs/api/video.html
@js.native
@JSName("videojs")
object VideoJS extends js.Object {
  def apply(element: String | dom.Element, settings: js.Object = ???, ready: js.Function = ???): Unit = js.native
}