package com.skysam.speakers.dataClasses

import java.util.Date

data class SpeakerToView(
    val id: String,
    var name: String,
    var congregation: String,
    var lastDate: Date?,
    var observations: String
)
