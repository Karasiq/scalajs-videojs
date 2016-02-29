import sbt.Keys._

// Settings
val scalaJsLibraryName: String = "videojs"

lazy val commonSettings = Seq(
  organization := "com.github.karasiq",
  isSnapshot := false,
  version := "1.0.0",
  scalaVersion := "2.11.7"
)

lazy val librarySettings = Seq(
  name := s"scalajs-$scalaJsLibraryName",
  libraryDependencies ++= Seq(
    "org.scala-js" %%% "scalajs-dom" % "0.8.2"
  ),
  jsDependencies ++= {
    val videoJs = "org.webjars.bower" % "video-js" % "5.7.1"
    Seq(videoJs / "dist/video.min.js")
  },
  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  publishArtifact in Test := false,
  pomIncludeRepository := { _ ⇒ false },
  licenses := Seq("The MIT License" → url("http://opensource.org/licenses/MIT")),
  homepage := Some(url(s"https://github.com/Karasiq/${name.value}")),
  pomExtra := <scm>
    <url>git@github.com:Karasiq/${name.value}.git</url>
    <connection>scm:git:git@github.com:Karasiq/${name.value}.git</connection>
  </scm>
    <developers>
      <developer>
        <id>karasiq</id>
        <name>Piston Karasiq</name>
        <url>https://github.com/Karasiq</url>
      </developer>
    </developers>
)

lazy val testBackendSettings = Seq(
  name := s"scalajs-$scalaJsLibraryName-test",
  resolvers += Resolver.sonatypeRepo("snapshots"),
  libraryDependencies ++= {
    val sprayV = "1.3.3"
    val akkaV = "2.4.0"
    Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaV,
      "io.spray" %% "spray-can" % sprayV,
      "io.spray" %% "spray-routing-shapeless2" % sprayV,
      "io.spray" %% "spray-json" % "1.3.2"
    )
  },
  mainClass in Compile := Some("com.karasiq.scalajstest.backend.TestApp"),
  scalaJsBundlerInline in Compile := true,
  scalaJsBundlerCompile in Compile <<= (scalaJsBundlerCompile in Compile).dependsOn(fullOptJS in Compile in libraryTestFrontend),
  scalaJsBundlerAssets in Compile += {
    import com.karasiq.scalajsbundler.dsl.{Script, _}
    val videoJs = github("videojs", "video.js", "5.8.0") / "dist"
    val jsDeps = Seq(
      // jQuery
      Script from url("https://code.jquery.com/jquery-2.1.4.min.js"),

      // Video.js
      Script from url(videoJs % "video.min.js"),
      Style from url(videoJs % "video-js.min.css"),
      Static("video-js.swf") from url(videoJs % "video-js.swf"),

      // Plugins
      Script from url(github("eXon", "videojs-youtube", "2.0.8") % "dist/Youtube.min.js")
    )

    val appFiles = Seq(
      // Static
      Html from TestPageAssets.index,

      // Scala.js app
      Script from file("test") / "frontend" / "target" / "scala-2.11" / s"scalajs-$scalaJsLibraryName-test-frontend-opt.js",
      Script from file("test") / "frontend" / "target" / "scala-2.11" / s"scalajs-$scalaJsLibraryName-test-frontend-launcher.js"
    )

    val fonts = fontPackage("VideoJS", videoJs % "font/VideoJS", "font", Seq("eot", "svg", "ttf", "woff"))

    Bundle("index", jsDeps ++ appFiles ++ fonts: _*)
  }
)

lazy val testFrontendSettings = Seq(
  persistLauncher in Compile := true,
  name := s"scalajs-$scalaJsLibraryName-test-frontend",
  libraryDependencies ++= Seq(
    "be.doeraene" %%% "scalajs-jquery" % "0.8.1"
  )
)

// Projects
lazy val library = Project("scalajs-library", file("."))
  .settings(commonSettings, librarySettings)
  .enablePlugins(ScalaJSPlugin)

lazy val libraryTest = Project(s"scalajs-$scalaJsLibraryName-test", file("test"))
  .settings(commonSettings, testBackendSettings)
  .enablePlugins(ScalaJSBundlerPlugin)

lazy val libraryTestFrontend = Project(s"scalajs-$scalaJsLibraryName-test-frontend", file("test") / "frontend")
  .settings(commonSettings, testFrontendSettings)
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(library)