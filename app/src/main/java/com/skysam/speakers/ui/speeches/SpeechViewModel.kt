package com.skysam.speakers.ui.speeches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.skysam.speakers.dataClasses.Convention
import com.skysam.speakers.dataClasses.Speaker
import com.skysam.speakers.dataClasses.Speech
import com.skysam.speakers.repositories.Speakers
import com.skysam.speakers.repositories.Speeches

class SpeechViewModel: ViewModel() {
    private val _convention = MutableLiveData<Convention>()
    val convention: LiveData<Convention> get() = _convention

    fun getSpeeches(convention: Convention): LiveData<List<Speech>> {
        return Speeches.getSpeechesByConvention(convention.id).asLiveData()
    }

    fun getSpeakers(speech: Speech): LiveData<List<Speaker>> {
        return Speakers.getSpeakersBySpeech(speech.id).asLiveData()
    }

    fun viewConvention(convention: Convention) {
        _convention.value = convention
    }
}