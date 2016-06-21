package com.karasiq.scalajstest.frontend

import com.karasiq.videojs._
import org.scalajs.dom
import org.scalajs.dom.Element
import org.scalajs.jquery._

import scala.language.postfixOps
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

@JSExport
object TestApp extends JSApp {
  @JSExport
  override def main(): Unit = {
    jQuery(() ⇒ {
      val videos = Seq(renderWebm(), renderYoutube())
      videos.foreach(dom.document.body.appendChild)
    })
  }

  private def renderWebm(): Element = {
    VideoJSBuilder()
      .sources(VideoSource("video/webm", "http://video.webmfiles.org/elephants-dream.webm"))
      .controls(true)
      .poster("http://www.webmfiles.org/wp-content/uploads/2010/05/webm-files.jpg")
      .dimensions(640, 360)
      .ready { player ⇒
        player.bigPlayButton.el().setAttribute("style", "color: red;")
        player.on("ended", () ⇒ dom.console.log("Video on ended"))
      }
      .build()
  }

  private def renderYoutube(): Element = {
    VideoJSBuilder()
      .techOrder("youtube")
      .sources(VideoSource("video/youtube", "https://www.youtube.com/watch?v=xjS6SftYQaQ"))
      .controls(true)
      .dimensions(640, 360)
      .ready { video ⇒
        video.playbackRate(0.5)
        video.play()
      }
      .options("iv_load_policy" → 1)
      .build()
  }
}