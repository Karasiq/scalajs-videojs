package com.karasiq.videojs.components

import com.karasiq.videojs.Component

import scala.scalajs.js

@js.native
trait VolumeControl extends Component {
  val volumeBar: VolumeBar = js.native
}
