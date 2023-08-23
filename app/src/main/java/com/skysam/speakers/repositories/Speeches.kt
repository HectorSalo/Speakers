package com.skysam.speakers.repositories

import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
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
        return FirebaseFirestore.getInstance().collection(PATH)
    }

    fun getSpeeches(): Flow<List<Speech>> {
        return callbackFlow {
            val request = getInstance()
                .addSnapshotListener (MetadataChanges.INCLUDE) { value, error ->
                    if (error != null) {
                        Log.w(ContentValues.TAG, "Listen failed.", error)
                        return@addSnapshotListener
                    }

                    val speeches = mutableListOf<Speech>()
                    for (speech in value!!) {
                        var speakers = listOf<String>()
                        if (speech.get(Constants.SPEAKERS) != null) {
                            @Suppress("UNCHECKED_CAST")
                            speakers = speech.data.getValue(Constants.SPEAKERS) as List<String>
                        }
                        val newSpeech = Speech(
                            speech.id,
                            speech.getString(Constants.TITLE)!!,
                            speech.getString(Constants.ID_CONVENTION)!!,
                            speakers
                        )
                        speeches.add(newSpeech)
                    }
                    trySend(speeches)
                }
            awaitClose { request.remove() }
        }
    }
}