package be.dekade.weekplanner

import android.app.Application
import androidx.lifecycle.*
import be.dekade.weekplanner.data.*
import be.dekade.weekplanner.helpers.NotificationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
@HiltViewModel
class NieuweActiviteitViewModel @Inject internal constructor(
    val activiteitEnDagGegevensRepository: ActiviteitEnDagGegevensRepository,
    application: Application
): AndroidViewModel(application) {
    private val _eventActiviteitSubmitted = MutableLiveData<Boolean>()
    val eventActiviteitSubmitted: LiveData<Boolean>
        get() = _eventActiviteitSubmitted


    private val _eventTitelVoiceInput = MutableLiveData<Boolean>()
    val eventTitelVoiceInput: LiveData<Boolean>
        get() = _eventTitelVoiceInput


    private val _eventNotitiesVoiceInput = MutableLiveData<Boolean>()
    val evenNotitiesVoiceInput: LiveData<Boolean>
        get() = _eventNotitiesVoiceInput

    val activiteit = MutableLiveData<ActiviteitData>()

    private var _foutmelding = MutableLiveData<String>()
    val foutmelding: LiveData<String>
        get() = _foutmelding

    var isMaandag = false
    var isDinsdag = false
    var isWoensdag = false
    var isDonderdag = false
    var isVrijdag = false
    var isZaterdag = false
    var isZondag = false

    init {
        activiteit.value = ActiviteitData(titel = "", notities =  "", startuur =  8, startminuut =  0, isNotificatieAan =  true)
        _eventActiviteitSubmitted.value = false
        _eventNotitiesVoiceInput.value = false
        _eventTitelVoiceInput.value = false
    }

    fun submitActiviteit(){
        if(activiteit.value?.titel.isNullOrEmpty()){
            _foutmelding.value = "Vul de titel in."
        }else if (!(isZondag || isZaterdag || isVrijdag || isDonderdag || isWoensdag || isDinsdag || isMaandag)){
            _foutmelding.value = "Vink minstens 1 dag aan"
        }else {
            viewModelScope.launch {
                insert(activiteit.value!!)
                _eventActiviteitSubmitted.value = true
            }
        }
    }

    fun startVoiceInputTitel(){
        _eventTitelVoiceInput.value = true
    }

    fun startVoiceInputNotities(){
        _eventNotitiesVoiceInput.value = true
    }

    private suspend fun insert(activiteit: ActiviteitData){
        withContext(Dispatchers.IO){
            val dagGegevens = listOf<DagGegevensData>(
                DagGegevensData(activiteit = activiteit, dag= Calendar.MONDAY, isActief =  isMaandag, uitstelUur =  -1, uitstelMinuut = -1, isAfgewerkt = false),
                DagGegevensData(activiteit = activiteit, dag= Calendar.TUESDAY, isActief =  isDinsdag, uitstelUur =  -1, uitstelMinuut = -1, isAfgewerkt = false),
                DagGegevensData(activiteit = activiteit, dag= Calendar.WEDNESDAY, isActief =  isWoensdag, uitstelUur =  -1, uitstelMinuut = -1, isAfgewerkt = false),
                DagGegevensData(activiteit = activiteit, dag= Calendar.THURSDAY, isActief = isDonderdag, uitstelUur =  -1, uitstelMinuut = -1, isAfgewerkt = false),
                DagGegevensData(activiteit = activiteit, dag= Calendar.FRIDAY, isActief = isVrijdag, uitstelUur =  -1, uitstelMinuut = -1, isAfgewerkt = false),
                DagGegevensData(activiteit = activiteit, dag= Calendar.SATURDAY, isActief = isZaterdag, uitstelUur =  -1, uitstelMinuut = -1, isAfgewerkt = false),
                DagGegevensData(activiteit = activiteit, dag= Calendar.SUNDAY, isActief = isZondag, uitstelUur =  -1, uitstelMinuut = -1, isAfgewerkt = false)
            )
            activiteit.dagGegevens = dagGegevens
            NotificationHelper.setNotifications(activiteit, getApplication())
            activiteitEnDagGegevensRepository.postActiviteit(activiteit)
        }
    }

    fun onSubmitComplete(){
        _eventActiviteitSubmitted.value = false
    }

    fun onFoutmeldingHandled(){
        _foutmelding.value = ""
    }

    fun onVoiceInputComplete(){
        _eventTitelVoiceInput.value = false
        _eventNotitiesVoiceInput.value = false
    }
}