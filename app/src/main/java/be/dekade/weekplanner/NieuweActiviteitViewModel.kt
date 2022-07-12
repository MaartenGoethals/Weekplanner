package be.dekade.weekplanner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.dekade.weekplanner.data.Activiteit
import be.dekade.weekplanner.data.ActiviteitEnDagGegevensRepository
import be.dekade.weekplanner.data.DagGegevens
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class NieuweActiviteitViewModel @Inject internal constructor(
    val activiteitEnDagGegevensRepository: ActiviteitEnDagGegevensRepository
): ViewModel() {
    private val _eventActiviteitSubmitted = MutableLiveData<Boolean>()
    val eventActiviteitSubmitted: LiveData<Boolean>
        get() = _eventActiviteitSubmitted

    val activiteit = MutableLiveData<Activiteit>()

    var isMaandag = false
    var isDinsdag = false
    var isWoensdag = false
    var isDonderdag = false
    var isVrijdag = false
    var isZaterdag = false
    var isZondag = false

    init {
        activiteit.value = Activiteit(0,"", "", 8, 0, true)
        _eventActiviteitSubmitted.value = false
    }

    fun submitActiviteit(){
        //TODO implement domeinregels
        viewModelScope.launch {
            insert(activiteit.value!!)
            _eventActiviteitSubmitted.value = true
        }
    }

    private suspend fun insert(activiteit: Activiteit){
        withContext(Dispatchers.IO){
            var activiteitId = activiteitEnDagGegevensRepository.postActiviteit(activiteit)
            var dagGegevens = listOf<DagGegevens>(
                DagGegevens(0,activiteitId, 0, isMaandag, -1,-1,false),
                DagGegevens(0,activiteitId, 1, isDinsdag, -1,-1,false),
                DagGegevens(0,activiteitId, 2, isWoensdag, -1,-1,false),
                DagGegevens(0,activiteitId, 3, isDonderdag, -1,-1,false),
                DagGegevens(0,activiteitId, 4, isVrijdag, -1,-1,false),
                DagGegevens(0,activiteitId, 5, isZaterdag, -1,-1,false),
                DagGegevens(0,activiteitId, 6, isZondag, -1,-1,false)
            )
            activiteitEnDagGegevensRepository.postDagGegevens(dagGegevens)
        }
    }

    fun onSubmitComplete(){
        _eventActiviteitSubmitted.value = false
    }
}