package com.skysam.speakers.repositories

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Query
import com.skysam.speakers.common.Constants
import com.skysam.speakers.common.Utils
import com.skysam.speakers.dataClasses.Convention
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.util.Date

/**
 * Created by Hector Chirinos on 22/08/2023.
 */

object Conventions {
 private val PATH = when(Utils.getEnviroment()) {
  Constants.DEMO -> Constants.CONVENTIONS_DEMO
  Constants.RELEASE -> Constants.CONVENTIONS
  else -> Constants.CONVENTIONS
 }

 private fun getInstance(): CollectionReference {
  //return FirebaseFirestore.getInstance().collection(PATH)
  return FirebaseFirestore.getInstance().collection("conventions")
 }

 fun getConventions(): Flow<List<Convention>> {
  return callbackFlow {
   val request = getInstance()
    .orderBy(Constants.DATE_A, Query.Direction.ASCENDING)
    .addSnapshotListener (MetadataChanges.INCLUDE) { value, error ->
     if (error != null) {
      Log.w(TAG, "Listen failed.", error)
      return@addSnapshotListener
     }

     val conventions = mutableListOf<Convention>()
     for (convention in value!!) {
      val dateA = if (convention.getDate(Constants.DATE_A) != null) convention.getDate(Constants.DATE_A) else null
      val dateB = if (convention.getDate(Constants.DATE_B) != null) convention.getDate(Constants.DATE_B) else null
      var speeches = listOf<String>()
      if (convention.get(Constants.SPEECHES) != null) {
       @Suppress("UNCHECKED_CAST")
       speeches = convention.data.getValue(Constants.SPEECHES) as List<String>
      }
      val newConvention = Convention(
       convention.id,
       convention.getString(Constants.TITLE)!!,
       convention.getString(Constants.IMAGE)!!,
       dateA,
       dateB,
       speeches
      )
      conventions.add(newConvention)
     }
     trySend(conventions)
    }
   awaitClose { request.remove() }
  }
 }

 fun getCurrentConventions(): Flow<List<Convention>> {
  return callbackFlow {
   val request = getInstance()
    .whereEqualTo(Constants.CURRENT, true)
    .addSnapshotListener (MetadataChanges.INCLUDE) { value, error ->
     if (error != null) {
      Log.w(TAG, "Listen failed.", error)
      return@addSnapshotListener
     }

     val conventions = mutableListOf<Convention>()
     for (convention in value!!) {
      val dateA = if (convention.getDate(Constants.DATE_A) != null) convention.getDate(Constants.DATE_A) else null
      val dateB = if (convention.getDate(Constants.DATE_B) != null) convention.getDate(Constants.DATE_B) else null
      var speeches = listOf<String>()
      if (convention.get(Constants.SPEECHES) != null) {
       @Suppress("UNCHECKED_CAST")
       speeches = convention.data.getValue(Constants.SPEECHES) as List<String>
      }
      val newConvention = Convention(
       convention.id,
       convention.getString(Constants.TITLE)!!,
       convention.getString(Constants.IMAGE)!!,
       dateA,
       dateB,
       speeches
      )
      conventions.add(newConvention)
     }
     trySend(conventions)
    }
   awaitClose { request.remove() }
  }
 }

 fun getCurrentConventionsAvalible(): Flow<List<Convention>> {
  return callbackFlow {
   val request = getInstance()
    .whereEqualTo(Constants.CURRENT, true)
    .addSnapshotListener (MetadataChanges.INCLUDE) { value, error ->
     if (error != null) {
      Log.w(TAG, "Listen failed.", error)
      return@addSnapshotListener
     }

     val conventions = mutableListOf<Convention>()
     for (convention in value!!) {
      val dateA = if (convention.getDate(Constants.DATE_A) != null) convention.getDate(Constants.DATE_A) else null
      val dateB = if (convention.getDate(Constants.DATE_B) != null) convention.getDate(Constants.DATE_B) else null
      var speeches = listOf<String>()
      if (convention.get(Constants.SPEECHES) != null) {
       @Suppress("UNCHECKED_CAST")
       speeches = convention.data.getValue(Constants.SPEECHES) as List<String>
      }
      val newConvention = Convention(
       convention.id,
       convention.getString(Constants.TITLE)!!,
       convention.getString(Constants.IMAGE)!!,
       dateA,
       dateB,
       speeches
      )
      conventions.add(newConvention)
     }
     trySend(conventions)
    }
   awaitClose { request.remove() }
  }
 }

 fun getLastConventionFromSpeaker(speechesFrom: List<String>): Flow<Convention> {
  return callbackFlow {
   getInstance()
    .whereArrayContainsAny(Constants.SPEECHES, speechesFrom)
    .orderBy(Constants.DATE_B, Query.Direction.DESCENDING)
    .limit(1)
    .get()
    .addOnSuccessListener {value ->
     val conventions = mutableListOf<Convention>()
     for (convention in value!!) {
      val dateA = if (convention.getDate(Constants.DATE_A) != null) convention.getDate(Constants.DATE_A) else null
      val dateB = if (convention.getDate(Constants.DATE_B) != null) convention.getDate(Constants.DATE_B) else null
      var speeches = listOf<String>()
      if (convention.get(Constants.SPEECHES) != null) {
       @Suppress("UNCHECKED_CAST")
       speeches = convention.data.getValue(Constants.SPEECHES) as List<String>
      }
      val newConvention = Convention(
       convention.id,
       convention.getString(Constants.TITLE)!!,
       convention.getString(Constants.IMAGE)!!,
       dateA,
       dateB,
       speeches
      )
      conventions.add(newConvention)
     }
     trySend(conventions[0])
    }
    .addOnFailureListener { exception ->
     Log.w(TAG, "Error getting documents: ", exception)
    }
   awaitClose {  }
  }
 }

 fun setDates(convention: Convention) {
  getInstance()
   .document(convention.id)
   .update(Constants.DATE_A, convention.dateA,
    Constants.DATE_B, convention.dateB)
 }

 fun addConvention() {
  val data = hashMapOf(
   Constants.TITLE to "Entremos en el descanso de Dios",
   Constants.DATE_A to null,
   Constants.DATE_B to null,
   Constants.CURRENT to true,
   Constants.IMAGE to "",
   Constants.SPEECHES to listOf<String>()
  )
  FirebaseFirestore.getInstance().collection("conventions")
   .add(data)
 }

 fun associateSpeeches(id: String, speeches: List<String>) {
  FirebaseFirestore.getInstance().collection("conventions")
   .document(id)
   .update(Constants.SPEECHES, speeches)
 }
}