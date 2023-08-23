package com.skysam.speakers.dataClasses

import java.util.Date

/**
 * Created by Hector Chirinos on 22/08/2023.
 */

data class Convention(
 val id: String,
 var title: String,
 var image: String,
 var dateA: Date?,
 var dateB: Date?,
 var speeches: List<String>
)
