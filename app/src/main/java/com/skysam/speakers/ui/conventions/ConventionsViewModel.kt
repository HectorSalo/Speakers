package com.skysam.speakers.ui.conventions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.skysam.speakers.dataClasses.Convention
import com.skysam.speakers.repositories.Conventions

class ConventionsViewModel : ViewModel() {
    val conventions: LiveData<List<Convention>> = Conventions.getConventions().asLiveData()
}