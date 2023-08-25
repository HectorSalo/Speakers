package com.skysam.speakers.ui.speakers

import com.skysam.speakers.dataClasses.Speaker

/**
 * Created by Hector Chirinos on 24/08/2023.
 */

interface OnClick {
 fun view(speaker: Speaker)
 fun assign(speaker: Speaker)
 fun update(speaker: Speaker)
 fun enable(speaker: Speaker)
 fun delete(speaker: Speaker)
}