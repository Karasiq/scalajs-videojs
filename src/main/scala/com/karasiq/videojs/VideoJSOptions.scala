package com.karasiq.videojs

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.UndefOr
import scala.scalajs.js.annotation.ScalaJSDefined

@js.native
trait VideoSource extends js.Object {
  val `type`: String = js.native
  val src: String = js.native
}

object VideoSource {
  def apply(`type`: String, src: String): VideoSource = {
    js.Dynamic.literal(`type` = `type`, src = src).asInstanceOf[VideoSource]
  }
}

object VideoJSOptions {
  def apply(sources: Seq[VideoSource], controls: Boolean = true, autoplay: Boolean = false, loop: Boolean = false, fluid: Boolean = false, preload: String = "auto", poster: UndefOr[String] = js.undefined, width: UndefOr[Int] = js.undefined, height: UndefOr[Int] = js.undefined, techOrder: Seq[String] = Nil, additional: Seq[(String, js.Any)] = Nil): VideoJSOptions = {
    val obj = js.Object().asInstanceOf[js.Dynamic]
    obj.controls = controls
    obj.autoplay = autoplay
    obj.fluid = fluid
    obj.loop = loop
    obj.preload = preload
    if (poster.nonEmpty) obj.poster = poster
    if (width.nonEmpty) obj.width = width
    if (height.nonEmpty) obj.height = height
    if (sources.nonEmpty) obj.sources = sources.toJSArray
    if (techOrder.nonEmpty) obj.techOrder = techOrder.toJSArray
    additional.foreach { case (key, value) ⇒ obj.updateDynamic(key)(value) }
    obj.asInstanceOf[VideoJSOptions]
  }
}

@ScalaJSDefined
class VideoJSOptions extends js.Object {
  /**
    * The controls option sets whether or not the player has controls that the user can interact with. Without controls the only way to start the video playing is with the autoplay attribute or through the API.
    */
  val controls: Boolean = false

  /**
    * If autoplay is true, the video will start playing as soon as page is loaded (without any interaction from the user). NOT SUPPORTED BY APPLE iOS DEVICES. Apple blocks the autoplay functionality in an effort to protect it's customers from unwillingly using a lot of their (often expensive) monthly data plans. A user touch/click is required to start the video in this case.
    */
  val autoplay: Boolean = false

  /**
    * The loop attribute causes the video to start over as soon as it ends. This could be used for a visual effect like clouds in the background.
    */
  val loop: Boolean = false

  /**
    * The preload attribute informs the browser whether or not the video data should begin downloading as soon as the video tag is loaded. The options are auto, metadata, and none.
    * 'auto': Start loading the video immediately (if the browser agrees). Some mobile devices like iPhones and iPads will not preload the video in order to protect their users' bandwidth. This is why the value is called 'auto' and not something more final like 'true'.
    * 'metadata': Load only the meta data of the video, which includes information like the duration and dimensions of the video.
    * 'none': Don't preload any of the video data. This will wait until the user clicks play to begin downloading.
    */
  val preload: String = "auto"

  /**
    * The poster attribute sets the image that displays before the video begins playing. This is often a frame of the video or a custom title screen. As soon as the user clicks play the image will go away.
    */
  val poster: UndefOr[String] = js.undefined

  /**
    * The width attribute sets the display width of the video.
    */
  val width: UndefOr[Int] = js.undefined

  /**
    * The height attribute sets the display height of the video.
    */
  val height: UndefOr[Int] = js.undefined

  val fluid: Boolean = false

  val techOrder: js.Array[String] = js.Array()

  val sources: js.Array[VideoSource] = js.Array()
}