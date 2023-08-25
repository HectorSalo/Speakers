package com.skysam.speakers.common

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.skysam.speakers.BuildConfig
import com.skysam.speakers.dataClasses.Convention
import com.skysam.speakers.dataClasses.Speaker
import java.text.Collator
import java.text.DateFormat
import java.util.Calendar
import java.util.Collections
import java.util.Comparator
import java.util.Date

/**
 * Created by Hector Chirinos on 21/08/2023.
 */

object Utils {
 fun getEnviroment(): String {
  return BuildConfig.BUILD_TYPE
 }

 fun close(view: View) {
  val imn = Speakers.Speakers.getContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imn.hideSoftInputFromWindow(view.windowToken, 0)
 }

 fun convertDateToString(value: Date): String {
  return DateFormat.getDateInstance().format(value)
 }

 fun organizedAlphabeticList(list: List<Speaker>): List<Speaker> {
  Collections.sort(list, object : Comparator<Speaker> {
   var collator = Collator.getInstance()
   override fun compare(p0: Speaker?, p1: Speaker?): Int {
    return collator.compare(p0?.name, p1?.name)
   }

  })
  return list
 }
}