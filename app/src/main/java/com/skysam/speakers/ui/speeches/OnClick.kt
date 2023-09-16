package com.skysam.speakers.ui.speeches

import com.skysam.speakers.dataClasses.SpeechToView

/**
 * Created by Hector Chirinos on 12/09/2023.
 */

interface OnClick {
 fun select(speechToView: SpeechToView)
}