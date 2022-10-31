import sbt.Keys._
import com.karasiq.scalajsbundler.compilers.AssetCompilers
import com.karasiq.scalajsbundler.dsl.{Script, _}

// Projects
lazy val library =
  (project in file("."))
    .settings(commonSettings, webpackSettings, librarySettings, publishSettings)
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)

lazy val testFrontend =
  (project in (file("test") / "frontend"))
    .settings(commonSettings, webpackSettings, testFrontendSettings, noPublishSettings)
    .enablePlugins(ScalaJSPlugin, ScalaJSBundlerPlugin)
    .dependsOn(library)

lazy val testBackend =
  (project in file("test"))
    .settings(commonSettings, testBackendSettings, noPublishSettings)
    .enablePlugins(SJSAssetBundlerPlugin)

lazy val all =
  (project in file("target/temp_project"))
    .aggregate(library, testFrontend, testBackend)

// Global settings
// Reload on .sbt change
Global / onChangedBuildSource := ReloadOnSourceChanges

// Settings
val LibName: String = "videojs"

lazy val commonSettings =
  Seq(
    organization := "com.github.karasiq",
    scalaVersion := "2.13.4"
  )

lazy val webpackSettings =
  inConfig(Compile)(Seq(
    webpackEmitSourceMaps := true,
    webpack / version := (if (ProjectDefs.scalaJSIs06)
                            "4.46.0"
                          else
                            "5.74.0"),
    npmExtraArgs ++= Seq("--openssl-legacy-provider", "--legacy-peer-deps")
  ))

lazy val librarySettings =
  Seq(
    name := s"scalajs-$LibName",
    crossScalaVersions := (if (ProjectDefs.scalaJSIs06)
                             Seq("2.11.12", "2.12.1", "2.13.4")
                           else
                             Seq("2.12.1", "2.13.4")),
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % (if (ProjectDefs.scalaJSIs06)
                                                                 "1.0.0"
                                                               else
                                                                 "2.0.0"),
    Compile / npmDependencies ++= Seq(
      "video.js" -> "*"
    )
  )

lazy val publishSettings =
  Seq(
    publishMavenStyle      := true,
    sonatypeSessionName    := s"scalajs-$LibName v${version.value}",
    publishConfiguration   := publishConfiguration.value.withOverwrite(true),
    publishTo              := sonatypePublishToBundle.value,
    Test / publishArtifact := false,
    pomIncludeRepository   := { _ => false },
    licenses               := Seq("The MIT License" -> url("http://opensource.org/licenses/MIT")),
    homepage               := Some(url(s"https://github.com/Karasiq/scalajs-$LibName")),
    scmInfo := Some(
      ScmInfo(
        new URL(s"https://github.com/Karasiq/scalajs-$LibName"),
        s"scm:git:git@github.com:Karasiq/scalajs-$LibName.git"
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
    scalaVersion := "2.11.12",
    name         := s"scalajs-$LibName-test-backend",
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
      scalaJsBundlerAssets += {

        val staticFiles =
          Seq(
            // Html file
            Html from TestPageAssets.index,

            // jQuery
            Script from url("https://code.jquery.com/jquery-2.1.4.min.js"),

            // Video.js
            Style from url("https://cdnjs.cloudflare.com/ajax/libs/video.js/7.20.3") / "video-js.css"
          )

        Bundle("index", staticFiles, SJSApps.bundlerApp(testFrontend, fastOpt = true).value)
      },
      scalaJsBundlerCompilers := AssetCompilers.keepJavaScriptAsIs
    )
  )

lazy val testFrontendSettings =
  Seq(
    evictionErrorLevel              := util.Level.Debug,
    scalaJSUseMainModuleInitializer := true,
    name                            := s"scalajs-$LibName-test-frontend",
    Compile / npmDependencies ++= Seq(
      "video.js"        -> "^7.20.3",
      "videojs-youtube" -> "^2.6.1",
      "jquery"          -> "^2.1.4"
    )
  )
