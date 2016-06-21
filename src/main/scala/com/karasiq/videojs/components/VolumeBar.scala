package com.karasiq.videojs.components

import com.karasiq.videojs.Component

import scala.scalajs.js

@js.native
trait VolumeBar extends Component {
  val volumeLevel: Component = js.native
  val volumeHandle: Component = js.native
}
