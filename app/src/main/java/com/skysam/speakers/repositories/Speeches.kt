package com.skysam.speakers.repositories

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.google.firebase.firestore.Query
import com.skysam.speakers.common.Constants
import com.skysam.speakers.common.Utils
import com.skysam.speakers.dataClasses.Speech
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

object Speeches {
    private val PATH = when(Utils.getEnviroment()) {
        Constants.DEMO -> Constants.SPEECHES_DEMO
        Constants.RELEASE -> Constants.SPEECHES
        else -> Constants.SPEECHES
    }

    private fun getInstance(): CollectionReference {
        //return FirebaseFirestore.getInstance().collection(PATH)
        return FirebaseFirestore.getInstance().collection("speeches")
    }

    fun getSpeeches(): Flow<List<Speech>> {
        return callbackFlow {
            val request = getInstance()
                .addSnapshotListener (MetadataChanges.INCLUDE) { value, error ->
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error)
                        return@addSnapshotListener
                    }

                    val speeches = mutableListOf<Speech>()
                    for (speech in value!!) {
                        val newSpeech = Speech(
                            speech.id,
                            speech.getString(Constants.TITLE)!!,
                            speech.getString(Constants.ID_CONVENTION)!!,
                            speech.getDouble(Constants.POSITION)!!.toInt(),
                            speech.getBoolean(Constants.IS_VIAJANTE)!!,
                            speech.getBoolean(Constants.IS_REPRESENTANTE)!!
                        )
                        speeches.add(newSpeech)
                    }
                    trySend(speeches)
                }
            awaitClose { request.remove() }
        }
    }

    fun getSpeechesByConvention(id: String): Flow<List<Speech>> {
        return callbackFlow {
            val request = getInstance()
                .whereEqualTo(Constants.ID_CONVENTION, id)
                .orderBy(Constants.POSITION, Query.Direction.ASCENDING)
                .addSnapshotListener (MetadataChanges.INCLUDE) { value, error ->
                    if (error != null) {
                        Log.w(TAG, "Listen failed.", error)
                        return@addSnapshotListener
                    }

                    val speeches = mutableListOf<Speech>()
                    for (speech in value!!) {
                        val newSpeech = Speech(
                            speech.id,
                            speech.getString(Constants.TITLE)!!,
                            speech.getString(Constants.ID_CONVENTION)!!,
                            speech.getDouble(Constants.POSITION)!!.toInt(),
                            speech.getBoolean(Constants.IS_VIAJANTE)!!,
                            speech.getBoolean(Constants.IS_REPRESENTANTE)!!
                        )
                        speeches.add(newSpeech)
                    }
                    trySend(speeches)
                }
            awaitClose { request.remove() }
        }
    }

    fun getSpeechesBySpeaker(speechesFrom: List<String>): Flow<List<Speech>> {
        return callbackFlow {
            val speeches = mutableListOf<Speech>()
            for (speech in speechesFrom) {
                getInstance()
                    .document(speech)
                    .get()
                    .addOnSuccessListener {value ->
                        if (value != null && value.exists()) {
                        val newSpeech = Speech(
                            value.id,
                            value.getString(Constants.TITLE)!!,
                            value.getString(Constants.ID_CONVENTION)!!,
                            value.getDouble(Constants.POSITION)!!.toInt(),
                            value.getBoolean(Constants.IS_VIAJANTE)!!,
                            value.getBoolean(Constants.IS_REPRESENTANTE)!!
                        )
                        speeches.add(newSpeech)
                            if (speech == speechesFrom.last()) {
                                trySend(speeches)
                            }
                    } else {
                        Log.d(TAG, "Current data: null")
                    } }
                    .addOnFailureListener {
                        Log.w(TAG, "Listen failed.")
                    }
            }
            awaitClose {  }
        }
    }

    fun assignToViajante(speech: Speech) {
        getInstance()
            .document(speech.id)
            .update(Constants.IS_VIAJANTE, true, Constants.IS_REPRESENTANTE, false)
    }

    fun assignToRepresentante(speech: Speech) {
        getInstance()
            .document(speech.id)
            .update(Constants.IS_REPRESENTANTE, true, Constants.IS_VIAJANTE, false)
    }

    fun assignSpeaker(id: String) {
        getInstance()
            .document(id)
            .update(Constants.IS_REPRESENTANTE, false, Constants.IS_VIAJANTE, false)
    }

    fun addSpeech() {
        val data = hashMapOf(
            Constants.TITLE to "Si servimos a Jehová, seremos felices",
            Constants.POSITION to 12,
            Constants.IS_VIAJANTE to false,
            Constants.IS_REPRESENTANTE to false,
            Constants.ID_CONVENTION to "wgpccyzCClwk6uJuXSmC"
        )
        FirebaseFirestore.getInstance().collection("speeches")
            .add(data)
    }

    fun getSpeechesToAssociate() {
        FirebaseFirestore.getInstance().collection("speeches")
            .whereEqualTo(Constants.ID_CONVENTION, "wgpccyzCClwk6uJuXSmC")
            .get()
            .addOnSuccessListener { documents ->
                val list = mutableListOf<String>()
                for (document in documents) {
                    list.add(document.id)
                }
                Conventions.associateSpeeches("wgpccyzCClwk6uJuXSmC", list)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents: ", exception)
            }
    }
}