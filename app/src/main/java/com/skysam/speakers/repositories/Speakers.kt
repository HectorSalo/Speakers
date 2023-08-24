package com.skysam.speakers.repositories

import android.content.ContentValues
import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.MetadataChanges
import com.skysam.speakers.common.Constants
import com.skysam.speakers.common.Utils
import com.skysam.speakers.dataClasses.Speaker
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Created by Hector Chirinos on 23/08/2023.
 */

object Speakers {
    private val PATH = when(Utils.getEnviroment()) {
        Constants.DEMO -> Constants.SPEAKERS_DEMO
        Constants.RELEASE -> Constants.SPEAKERS
        else -> Constants.SPEAKERS
    }

    private fun getInstance(): CollectionReference {
        return FirebaseFirestore.getInstance().collection(PATH)
    }

    fun getSpeakers(): Flow<List<Speaker>> {
        return callbackFlow {
            val request = getInstance()
                .addSnapshotListener (MetadataChanges.INCLUDE) { value, error ->
                    if (error != null) {
                        Log.w(ContentValues.TAG, "Listen failed.", error)
                        return@addSnapshotListener
                    }

                    val speakers = mutableListOf<Speaker>()
                    for (speaker in value!!) {
                        var speeches = listOf<String>()
                        if (speaker.get(Constants.SPEECHES) != null) {
                            @Suppress("UNCHECKED_CAST")
                            speeches = speaker.data.getValue(Constants.SPEECHES) as List<String>
                        }

                        val newSpeaker = Speaker(
                            speaker.id,
                            speaker.getString(Constants.NAME)!!,
                            speaker.getString(Constants.CONGREGATION)!!,
                            speaker.getString(Constants.SECTION)!!,
                            null,
                            speeches,
                            speaker.getString(Constants.OBSERVATIONS)!!,
                            speaker.getBoolean(Constants.IS_ACTIVE)!!
                        )
                        speakers.add(newSpeaker)
                    }
                    trySend(speakers)
                }
            awaitClose { request.remove() }
        }
    }
}