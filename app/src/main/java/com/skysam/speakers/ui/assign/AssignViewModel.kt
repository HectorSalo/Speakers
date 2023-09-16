package com.skysam.speakers.ui.assign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.skysam.speakers.dataClasses.Convention
import com.skysam.speakers.dataClasses.Speaker
import com.skysam.speakers.dataClasses.Speech
import com.skysam.speakers.repositories.Conventions
import com.skysam.speakers.repositories.Speakers
import com.skysam.speakers.repositories.Speeches

/**
 * Created by Hector Chirinos on 24/08/2023.
 */

class AssignViewModel: ViewModel() {
    val conventions: LiveData<List<Convention>> = Conventions.getCurrentConventionsAvalible().asLiveData()

    private val _convention = MutableLiveData<Convention>()
    val convention: LiveData<Convention> get() = _convention

    private val _speaker = MutableLiveData<Speaker>()
    val speaker: LiveData<Speaker> get() = _speaker

    fun selectConvention(convention: Convention) {
        _convention.value = convention
    }

    fun selectSpeaker(speaker: Speaker) {
        _speaker.value = speaker
    }

    fun getSpeeches(convention: Convention): LiveData<List<Speech>> {
        return Speeches.getSpeechesByConvention(convention.id).asLiveData()
    }

    fun getSpeakers(speech: Speech): LiveData<List<Speaker>> {
        return Speakers.getSpeakersBySpeech(speech.id).asLiveData()
    }
}