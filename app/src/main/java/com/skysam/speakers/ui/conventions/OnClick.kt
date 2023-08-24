package com.skysam.speakers.ui.conventions

import com.skysam.speakers.dataClasses.Convention

/**
 * Created by Hector Chirinos on 23/08/2023.
 */

interface OnClick {
 fun view(convention: Convention)
 fun dates(convention: Convention)
 fun speakers(convention: Convention)
}