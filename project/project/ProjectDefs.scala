//noinspection DuplicatedCode,ScalaPackageName

object ProjectDefs {
  val ScalaJSVersion: String = sys.props.getOrElse("SCALAJS_VERSION", "0.6.33")

  def scalaJSIs06: Boolean =
    ScalaJSVersion.startsWith("0.6.")
}
