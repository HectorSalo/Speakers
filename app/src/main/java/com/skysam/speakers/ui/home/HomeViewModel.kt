package com.skysam.speakers.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.skysam.speakers.dataClasses.Convention
import com.skysam.speakers.dataClasses.Speaker
import com.skysam.speakers.dataClasses.Speech
import com.skysam.speakers.repositories.Conventions
import com.skysam.speakers.repositories.Speakers
import com.skysam.speakers.repositories.Speeches

class HomeViewModel : ViewModel() {
    val speakers: LiveData<List<Speaker>> = Speakers.getSpeakers().asLiveData()
    val speeches: LiveData<List<Speech>> = Speeches.getSpeeches().asLiveData()
    val conventions: LiveData<List<Convention>> = Conventions.getCurrentConventions().asLiveData()

    fun addConvention() {
        Conventions.addConvention()
    }
    fun addSpeech() {
        Speeches.addSpeech()
    }

    fun associateSpeeches() {
        Speeches.getSpeechesToAssociate()
    }
}