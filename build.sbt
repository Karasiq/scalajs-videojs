import com.karasiq.scalajsbundler.compilers.{AssetCompilers, ConcatCompiler}
import com.karasiq.scalajsbundler.dsl.{Script, _}
import sbt.Def
import sbt.Keys._

// Global settings

// Reload on .sbt change
Global / onChangedBuildSource := ReloadOnSourceChanges

// Git versioning
enablePlugins(GitVersioning)

ThisBuild / git.useGitDescribe       := true
ThisBuild / git.uncommittedSignifier := None
ThisBuild / versionScheme            := Some("pvp")

def _isSnapshotByGit: Def.Initialize[Boolean] =
  Def.setting(git.gitCurrentTags.value.isEmpty || git.gitUncommittedChanges.value)

ThisBuild / version := (ThisBuild / version).value + (if (_isSnapshotByGit.value)
                                                        "-SNAPSHOT"
                                                      else
                                                        "")

// Settings
val ScalaJsLibraryName: String = "videojs"

lazy val commonSettings =
  Seq(
    organization := "com.github.karasiq",
    scalaVersion := "2.13.4"
  )

lazy val librarySettings =
  Seq(
    name := s"scalajs-$ScalaJsLibraryName",
    crossScalaVersions := (if (ProjectDefs.scalaJSIs06)
                             Seq("2.11.8", "2.12.1", "2.13.4")
                           else
                             Seq("2.12.1", "2.13.4")),
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "1.0.0"
    )
  ) ++ inConfig(Compile)(Seq(
    npmDependencies ++= Seq(
      "video.js" → "* 5.20.5"
    )
  ))

lazy val publishSettings =
  Seq(
    publishMavenStyle      := true,
    sonatypeSessionName    := s"scalajs-$ScalaJsLibraryName v${version.value}",
    publishConfiguration   := publishConfiguration.value.withOverwrite(true),
    publishTo              := sonatypePublishToBundle.value,
    Test / publishArtifact := false,
    pomIncludeRepository   := { _ ⇒ false },
    licenses               := Seq("The MIT License" → url("http://opensource.org/licenses/MIT")),
    homepage               := Some(url(s"https://github.com/Karasiq/scalajs-$ScalaJsLibraryName")),
    scmInfo := Some(
      ScmInfo(
        new URL(s"https://github.com/Karasiq/scalajs-$ScalaJsLibraryName"),
        s"scm:git:git@github.com:Karasiq/scalajs-$ScalaJsLibraryName.git"
      )
    ),
    developers := List(
      Developer(
        "Karasiq",
        "Piston Karasiq",
        "karasiq.gh.cru5k@simplelogin.co",
        new URL("https://github.com/Karasiq")
      )
    )
  )

lazy val noPublishSettings =
  Seq(
    publishArtifact           := false,
    makePom / publishArtifact := false,
    publishTo                 := Some(Resolver.file("Repo", file("target/repo")))
  )

lazy val testBackendSettings =
  Seq(
    scalaVersion := "2.11.8",
    name         := s"scalajs-$ScalaJsLibraryName-test-backend",
    // resolvers   ++= Resolver.sonatypeOssRepos("snapshots"),
    libraryDependencies ++= {
      val sprayV = "1.3.3"
      val akkaV  = "2.4.0"
      Seq(
        "com.typesafe.akka" %% "akka-actor"               % akkaV,
        "io.spray"          %% "spray-can"                % sprayV,
        "io.spray"          %% "spray-routing-shapeless2" % sprayV,
        "io.spray"          %% "spray-json"               % "1.3.2"
      )
    }
  ) ++ inConfig(Compile)(
    Seq(
      mainClass            := Some("com.com.karasiq.scalajstest.backend.TestApp"),
      scalaJsBundlerInline := false,
      scalaJsBundlerCompile := scalaJsBundlerCompile
        .dependsOn(testFrontend / fastOptJS)
        .value,
      compile := compile.dependsOn(scalaJsBundlerCompile).value,
      scalaJsBundlerAssets += {
        val videoJs = github("videojs", "video.js", "v5.8.0") / "dist"
        val jsDeps =
          Seq(
            // jQuery
            Script from url("https://code.jquery.com/jquery-2.1.4.min.js"),

            // Video.js
            Script from url(videoJs % "video.min.js"),
            Style from url(videoJs % "video-js.min.css"),
            Static("video-js.swf") from url(videoJs % "video-js.swf"),

            // Plugins
            Script from url(github("eXon", "videojs-youtube", "v2.0.8") % "dist/Youtube.min.js")
          )

        val appFiles =
          Seq(
            // Static
            Html from TestPageAssets.index,
            // Scala.js app
            TestPageAssets.sourceMap(testFrontend, fastOpt = true).value
          ) ++ scalaJsApplication(testFrontend, fastOpt = true).value

        val fonts = fontPackage("VideoJS", videoJs % "font/VideoJS", "font", Seq("eot", "svg", "ttf", "woff"))

        Bundle("index", jsDeps, appFiles, fonts)
      },
      Compile / scalaJsBundlerCompilers := AssetCompilers { case Mimes.javascript ⇒ ConcatCompiler }.<<=(
        AssetCompilers.default
      )
    )
  )

lazy val testFrontendSettings =
  Seq(
    scalaJSUseMainModuleInitializer := true,
    name                            := s"scalajs-$ScalaJsLibraryName-test-frontend",
    libraryDependencies ++= Seq(
      "be.doeraene" %%% "scalajs-jquery" % "1.0.0"
    )
  )

// Projects
lazy val library =
  (project in file("."))
    .settings(commonSettings, librarySettings, publishSettings)
    .enablePlugins(ScalaJSPlugin, scalajsbundler.sbtplugin.ScalaJSBundlerPlugin)

lazy val testBackend =
  (project in file("test"))
    .settings(commonSettings, testBackendSettings, noPublishSettings)
    .enablePlugins(SJSAssetBundlerPlugin)

lazy val testFrontend =
  (project in (file("test") / "frontend"))
    .settings(commonSettings, testFrontendSettings, noPublishSettings)
    .enablePlugins(ScalaJSPlugin)
    .dependsOn(library)
