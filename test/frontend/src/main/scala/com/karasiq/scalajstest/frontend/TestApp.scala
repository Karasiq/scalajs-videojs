package com.karasiq.scalajstest.frontend

import com.karasiq.videojs._
import org.scalajs.dom
import org.scalajs.dom.Element

import scala.language.postfixOps
import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

object TestApp extends App {
  @js.native
  @JSImport("videojs-youtube", JSImport.Default)
  object VideoJSYoutube extends js.Object

  VideoJSImport.initialize()
  // noinspection ScalaUnusedExpression
  VideoJSYoutube

  dom.window.addEventListener(
    "DOMContentLoaded",
    { _: dom.Event =>
      dom.window.asInstanceOf[js.Dynamic].SVideoJS = VideoJS

      val videos = Seq(renderWebm(), renderYoutube())
      videos.foreach(dom.document.body.appendChild)
    }
  )

  private def renderWebm(): Element =
    VideoJSBuilder()
      .sources(VideoSource("video/webm", "https://dl8.webmfiles.org/elephants-dream.webm"))
      .controls(true)
      .poster("http://www.webmfiles.org/wp-content/uploads/2010/05/webm-files.jpg")
      .dimensions(640, 360)
      .ready { player =>
        player.bigPlayButton.el().setAttribute("style", "color: red;")
        player.on("ended", () => dom.console.log("Video on ended"))
      }
      .build()

  private def renderYoutube(): Element =
    VideoJSBuilder()
      .techOrder("youtube")
      .sources(VideoSource("video/youtube", "https://www.youtube.com/watch?v=xjS6SftYQaQ"))
      .controls(true)
      .dimensions(640, 360)
      .ready { video =>
        video.playbackRate(0.5)
        video.play()
      }
      .options("iv_load_policy" -> 1)
      .build()
}
