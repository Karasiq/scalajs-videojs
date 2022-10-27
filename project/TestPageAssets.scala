import com.karasiq.scalajsbundler.ScalaJSBundler.PageContent
import com.karasiq.scalajsbundler.dsl.{Static, _}
import sbt.{Def, Project}
import scalatags.Text.all._

import scala.annotation.nowarn

object TestPageAssets {
  def index: String =
    "<!DOCTYPE html>" + html(
      head(
        base(href := "/"),
        meta(name := "viewport", content := "width=device-width, initial-scale=1.0")
      ),
      body(
        // Empty
      )
    )

  // TODO: Move to the bundler
  @nowarn
  def sourceMap(project: Project, fastOpt: Boolean = false): Def.Initialize[PageContent] =
    Def.setting {
      import sbt.Keys.{name, scalaVersion, target}
      import sbt.{project => _, _}

      val nameValue    = (name in project).value
      val targetValue  = (target in project).value
      val versionValue = (scalaVersion in project).value

      val output = targetValue / s"scala-${CrossVersion.binaryScalaVersion(versionValue)}"
      val sourceMapName =
        if (fastOpt)
          s"$nameValue-fastopt.js.map"
        else
          s"$nameValue-opt.js.map"

      Static(s"scripts/$sourceMapName") from (output / sourceMapName)
    }
}
