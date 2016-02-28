logLevel := Level.Warn

resolvers += Resolver.sonatypeRepo("snapshots")

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "0.6.5")

addSbtPlugin("com.github.karasiq" % "sbt-scalajs-bundler" % "1.0.5")

libraryDependencies ++= Seq(
  "com.lihaoyi" %% "scalatags" % "0.5.4",
  "com.google.javascript" % "closure-compiler" % "v20151216"
)