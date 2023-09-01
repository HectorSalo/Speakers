package com.skysam.speakers.dataClasses

import java.io.Serializable
import java.util.Date

/**
 * Created by Hector Chirinos on 22/08/2023.
 */

data class Speaker(
 val id: String,
 var name: String,
 var congregation: String,
 var isSectionA: Boolean,
 val speeches: List<String>,
 var observations: String,
 var isActive: Boolean
): Serializable
