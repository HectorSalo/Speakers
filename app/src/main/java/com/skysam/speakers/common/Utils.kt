package com.skysam.speakers.common

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.skysam.speakers.BuildConfig
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

 /*fun organizedAlphabeticList(list: MutableList<Booking>): MutableList<Booking> {
  Collections.sort(list, object : Comparator<Booking> {
   var collator = Collator.getInstance()
   override fun compare(p0: Booking?, p1: Booking?): Int {
    return collator.compare(p0?.name, p1?.name)
   }

  })
  return list
 }*/
}