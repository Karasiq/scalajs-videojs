package com.karasiq.videojs.components

import com.karasiq.videojs.Component

import scala.scalajs.js

@js.native
trait ProgressControl extends Component {
  val seekBar: SeekBar = js.native
}
