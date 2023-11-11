package com.cube.cubeacademy.lib.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cube.cubeacademy.lib.di.Repository
import com.cube.cubeacademy.lib.models.Nomination
import com.cube.cubeacademy.lib.models.Nominee
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NominationViewModel @Inject constructor() : ViewModel() {

    // nominations
    private var _nominations = MutableLiveData<List<Nomination>>()
    val nominations = _nominations

    // nominees
    private var _nominees = MutableLiveData<List<Nominee>>()
    val nominees = _nominees

    // nominations status
    private var _nominationsSuccessful = MutableLiveData<Boolean>()
    val nominationsSuccessful = _nominationsSuccessful

    // nominees status
    private var _nomineesSuccessful = MutableLiveData<Boolean>()
    val nomineesSuccessful = _nomineesSuccessful

    @Inject
    lateinit var repository: Repository

    // fetch nominations
    fun fetchNominations() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getAllNominations()
            nominationsSuccessful.postValue(response.isSuccessful)
            if (response.isSuccessful) {
                response.body()?.let { _nominations.postValue(it.data) }
            }
        }
    }

    // fetch nominees
    fun fetchNominees() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = repository.getAllNominees()
            _nomineesSuccessful.postValue(response.isSuccessful)
            if (response.isSuccessful) {
                response.body()?.let { _nominees.postValue(it.data) }
            }
        }
    }
}