package com.skysam.speakers.dataClasses

import java.util.Date

data class SpeechToView(
    var title: String,
    var sectionA: String,
    val sectionB: String,
    var lastDate: Date? =  null
)
