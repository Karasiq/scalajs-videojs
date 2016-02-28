package com.karasiq.scalajstest.frontend

import com.karasiq.videojs.{VideoJS, VideoJSOptions, VideoSource}
import org.scalajs.dom
import org.scalajs.jquery._

import scala.language.postfixOps
import scala.scalajs.js
import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._

@JSExport
object TestApp extends JSApp {
  @JSExport
  override def main(): Unit = {
    jQuery(() ⇒ {
      renderWebm()
      renderYoutube()
    })
  }

  private def renderWebm(): Unit = {
    val videoContainer = video(
      `class` := "video-js",
      "controls".attr := "",
      p(`class` := "vjs-no-js", "To view this video please enable JavaScript")
    ).render

    dom.document.body.appendChild(videoContainer)

    val sources = Seq(
      VideoSource("video/webm", "http://video.webmfiles.org/elephants-dream.webm")
    )
    val settings = VideoJSOptions(sources, controls = true, poster = "http://www.webmfiles.org/wp-content/uploads/2010/05/webm-files.jpg", width = 640, height = 360)
    dom.console.log(settings)
    VideoJS(videoContainer, settings, js.ThisFunction.fromFunction1 { video: js.Dynamic ⇒
      video.on("ended", () ⇒ {
        dom.console.log("Video on ended")
      })
    })
  }

  private def renderYoutube(): Unit = {
    val videoContainer = video(
      `class` := "video-js",
      "controls".attr := "",
      p(`class` := "vjs-no-js", "To view this video please enable JavaScript")
    ).render

    dom.document.body.appendChild(videoContainer)

    val sources = Seq(
      VideoSource("video/youtube", "https://www.youtube.com/watch?v=xjS6SftYQaQ")
    )
    val settings = VideoJSOptions(sources, controls = true, width = 640, height = 360, techOrder = Seq("youtube"), additional = Seq("youtube" → js.Dynamic.literal(iv_load_policy = 1)))
    dom.console.log(settings)
    VideoJS(videoContainer, settings, js.ThisFunction.fromFunction1 { video: js.Dynamic ⇒
      video.on("ended", () ⇒ {
        dom.console.log("Youtube on ended")
      })
    })
  }
}