package com.karasiq.videojs

import org.scalajs.dom

import scala.scalajs.js
import scala.scalajs.js.UndefOr

case class VideoJSBuilder(sources: Seq[VideoSource] = Nil, controls: Boolean = true, autoplay: Boolean = false, loop: Boolean = false, fluid: Boolean = false, preload: String = "auto", poster: UndefOr[String] = js.undefined, width: UndefOr[Int] = js.undefined, height: UndefOr[Int] = js.undefined, techOrder: Seq[String] = Nil, ready: js.Function = () ⇒ (), additional: Seq[(String, js.Any)] = Nil) {
  def sources(value: VideoSource*): VideoJSBuilder = copy(sources = value)
  def controls(value: Boolean): VideoJSBuilder = copy(controls = value)
  def autoplay(value: Boolean): VideoJSBuilder = copy(autoplay = value)
  def loop(value: Boolean): VideoJSBuilder = copy(loop = value)
  def fluid(value: Boolean): VideoJSBuilder = copy(fluid = value)
  def preload(value: String): VideoJSBuilder = copy(preload = value)
  def poster(value: String): VideoJSBuilder = copy(poster = value)
  def dimensions(width: Int, height: Int): VideoJSBuilder = copy(width = width, height = height)
  def width(value: Int): VideoJSBuilder = copy(width = value)
  def height(value: Int): VideoJSBuilder = copy(height = value)
  def techOrder(value: String*): VideoJSBuilder = copy(techOrder = value)
  def ready(value: Player ⇒ Unit): VideoJSBuilder = copy(ready = VjsUtils.ready(value))
  def options(opts: (String, js.Any)*): VideoJSBuilder = copy(additional = opts)

  def build(): dom.Element = {
    val videoContainer = dom.document.createElement("video")
    videoContainer.setAttribute("class", "video-js")
    val wrapper = dom.document.createElement("div")
    wrapper.appendChild(videoContainer)

    val settings = VideoJSOptions(sources, controls, autoplay, loop, fluid, preload, poster, width, height, techOrder, additional)
    VideoJS(videoContainer, settings, ready)
    wrapper
  }
}
