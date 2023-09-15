package com.skysam.speakers.ui.assign

import com.skysam.speakers.dataClasses.SpeakerToView

interface OnClick {
    fun select(speakerToView: SpeakerToView)
}