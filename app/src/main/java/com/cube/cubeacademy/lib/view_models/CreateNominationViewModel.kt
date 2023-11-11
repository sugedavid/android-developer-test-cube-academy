package com.cube.cubeacademy.lib.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cube.cubeacademy.lib.di.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateNominationViewModel @Inject constructor() : ViewModel() {

    // loading
    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading = _isLoading

    // create nomination status
    private var _createNominationsSuccessful = MutableLiveData<Boolean>()
    val createNominationsSuccessful = _createNominationsSuccessful

    @Inject
    lateinit var repository: Repository

    // create nominations
    fun createNomination(
        nomineeId: String,
        reason: String,
        process: String
    ) {
        _isLoading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.createNomination(nomineeId, reason, process)
            createNominationsSuccessful.postValue(response?.isSuccessful)
            _isLoading.postValue(false)
        }
    }
}