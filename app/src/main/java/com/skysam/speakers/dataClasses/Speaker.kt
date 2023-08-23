package com.skysam.speakers.dataClasses

import java.util.Date

/**
 * Created by Hector Chirinos on 22/08/2023.
 */

data class Speaker(
 val id: String,
 var name: String,
 var congregation: String,
 val speeches: List<String>,
 val listDates: List<Date>,
 var observations: String,
 var isActive: Boolean
)
