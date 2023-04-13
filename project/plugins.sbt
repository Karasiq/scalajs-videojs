logLevel := Level.Warn

//noinspection ScalaDeprecation
resolvers += Resolver.sonatypeRepo("snapshots")

addSbtPlugin("org.jetbrains.scala" % "sbt-ide-settings" % "1.1.1")

addSbtPlugin("org.scala-js"       % "sbt-scalajs"              % ProjectDefs.ScalaJSVersion)
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject" % "1.1.0")

if (ProjectDefs.scalaJSIs06)
  Seq(
    addSbtPlugin("ch.epfl.scala"                     % "sbt-scalajs-bundler-sjs06" % "0.19.0"),
    addSbtPlugin("com.thoughtworks.sbt-scala-js-map" % "sbt-scala-js-map"          % "4.1.0"),
    addSbtPlugin("com.github.karasiq"                % "sbt-scalajs-bundler-sjs06" % "1.2.2-PR2-SNAPSHOT")
  )
else
  Seq(
    addSbtPlugin("ch.epfl.scala"                     % "sbt-scalajs-bundler" % "0.21.0"),
    addSbtPlugin("com.thoughtworks.sbt-scala-js-map" % "sbt-scala-js-map"    % "4.1.1+9-562f62a2"),
    addSbtPlugin("com.github.karasiq"                % "sbt-scalajs-bundler" % "1.2.2-PR2-SNAPSHOT")
  )

addSbtPlugin("com.github.sbt" % "sbt-git"      % "2.0.0")
addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "3.9.14")

libraryDependencies ++= Seq(
  "com.google.javascript" % "closure-compiler" % "v20230411",
  "com.lihaoyi"          %% "scalatags"        % "0.11.1"
)
