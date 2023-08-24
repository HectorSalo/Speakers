package com.skysam.speakers.ui.speakers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.skysam.speakers.dataClasses.Convention
import com.skysam.speakers.dataClasses.Speaker
import com.skysam.speakers.repositories.Conventions
import com.skysam.speakers.repositories.Speakers

class SpeakersViewModel : ViewModel() {
    val conventions: LiveData<List<Convention>> = Conventions.getConventions().asLiveData()
    val speakers: LiveData<List<Speaker>> = Speakers.getSpeakers().asLiveData()
}