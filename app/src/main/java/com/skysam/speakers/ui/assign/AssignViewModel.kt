package com.skysam.speakers.ui.assign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.skysam.speakers.dataClasses.Convention
import com.skysam.speakers.dataClasses.Speech
import com.skysam.speakers.repositories.Conventions
import com.skysam.speakers.repositories.Speeches

/**
 * Created by Hector Chirinos on 24/08/2023.
 */

class AssignViewModel: ViewModel() {
    val conventions: LiveData<List<Convention>> = Conventions.getCurrentConventionsAvalible().asLiveData()

    private val _convention = MutableLiveData<Convention>()
    val convention: LiveData<Convention> get() = _convention

    fun view(convention: Convention) {
        _convention.value = convention
    }

    fun getSpeeches(convention: Convention): LiveData<List<Speech>> {
        return Speeches.getSpeechesByConvention(convention.id).asLiveData()
    }
}