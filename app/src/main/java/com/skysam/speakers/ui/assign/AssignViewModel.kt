package com.skysam.speakers.ui.assign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.skysam.speakers.dataClasses.Convention
import com.skysam.speakers.dataClasses.Speaker
import com.skysam.speakers.dataClasses.SpeakerToView
import com.skysam.speakers.dataClasses.Speech
import com.skysam.speakers.repositories.Conventions
import com.skysam.speakers.repositories.Speakers
import com.skysam.speakers.repositories.Speeches

/**
 * Created by Hector Chirinos on 24/08/2023.
 */

class AssignViewModel: ViewModel() {
    val conventions: LiveData<List<Convention>> = Conventions.getCurrentConventionsAvalible().asLiveData()
    val speeches: LiveData<List<Speech>> = Speeches.getSpeeches().asLiveData()
    val speakers: LiveData<List<Speaker>> = Speakers.getSpeakers().asLiveData()

    private val _convention = MutableLiveData<Convention>()
    val convention: LiveData<Convention> get() = _convention

    private val _speaker = MutableLiveData<Speaker>()
    val speaker: LiveData<Speaker> get() = _speaker

    private val _isSectionA = MutableLiveData<Boolean>()
    val isSectionA: LiveData<Boolean> get() = _isSectionA

    private val _speakerSelectedDialog = MutableLiveData<SpeakerToView?>().apply { value = null }
    val speakerSelectedDialog: LiveData<SpeakerToView?> get() = _speakerSelectedDialog

    fun selectConvention(convention: Convention) {
        _convention.value = convention
    }

    fun selectSpeaker(speaker: Speaker) {
        _speaker.value = speaker
    }

    fun setSection(isSectionA: Boolean) {
        _isSectionA.value = isSectionA
    }

    fun getSpeakersBySection(isSectionA: Boolean): LiveData<List<Speaker>> {
        return Speakers.getSpeakersBySection(isSectionA).asLiveData()
    }

    fun getLastConventionFromSpeaker(speaker: Speaker): LiveData<Convention> {
        return Conventions.getLastConventionFromSpeaker(speaker.speeches).asLiveData()
    }

    fun selectSpeakerDialog(speakerToView: SpeakerToView?) {
        _speakerSelectedDialog.value = speakerToView
    }

    fun assignViajante(speech: Speech) {
        Speeches.assignToViajante(speech)
    }

    fun assignRepresentante(speech: Speech) {
        Speeches.assignToRepresentante(speech)
    }

    fun assignSpeaker(idSpeakers: List<Speaker>, idSpeech: String, id: String) {
        Speakers.assignSpeaker(idSpeakers, idSpeech, id)
        Speeches.assignSpeaker(idSpeech)
    }
}