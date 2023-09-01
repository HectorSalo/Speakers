package com.skysam.speakers.dataClasses

/**
 * Created by Hector Chirinos on 22/08/2023.
 */

data class Speech(
 val id: String,
 var title: String,
 var conventionId: String,
 val position: Int,
 var isViajante: Boolean,
 var isRepresentante: Boolean
)
