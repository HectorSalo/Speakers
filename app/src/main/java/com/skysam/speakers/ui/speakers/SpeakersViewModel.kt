package com.skysam.speakers.ui.speakers

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

class SpeakersViewModel : ViewModel() {
    val conventions: LiveData<List<Convention>> = Conventions.getConventions().asLiveData()
    val speakers: LiveData<List<Speaker>> = Speakers.getSpeakers().asLiveData()
    val speeches: LiveData<List<Speech>> = Speeches.getSpeeches().asLiveData()

    private val _speakerToUpdate = MutableLiveData<Speaker>()
    val speakerToUpdate: LiveData<Speaker> get() = _speakerToUpdate

    private val _speakerToView = MutableLiveData<Speaker>()
    val speakerToView: LiveData<Speaker> get() = _speakerToView

    fun saveSpeaker(speaker: Speaker) {
        Speakers.saveSpeaker(speaker)
    }

    fun speakerToUpdate(speaker: Speaker) {
        _speakerToUpdate.value = speaker
    }

    fun speakerToView(speaker: Speaker) {
        _speakerToView.value = speaker
    }

    fun updateSpeaker(speaker: Speaker) {
        Speakers.updateSpeaker(speaker)
    }

    fun enableSpeaker(speaker: Speaker) {
        Speakers.enableSpeaker(speaker)
    }

    fun deleteSpeaker(speaker: Speaker) {
        Speakers.deleteSpeaker(speaker)
    }

    fun getSpeechesBySpeaker(speaker: Speaker): LiveData<List<Speech>> {
        return Speeches.getSpeechesBySpeaker(speaker.speeches).asLiveData()
    }

    fun getLastConventionFromSpeaker(speaker: Speaker): LiveData<Convention> {
        return Conventions.getLastConventionFromSpeaker(speaker.speeches).asLiveData()
    }
}