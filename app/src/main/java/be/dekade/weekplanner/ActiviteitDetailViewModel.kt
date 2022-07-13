package be.dekade.weekplanner

import androidx.lifecycle.*
import be.dekade.weekplanner.data.ActiviteitEnDagGegevensRepository
import be.dekade.weekplanner.data.ActiviteitEnDagGegevensWeek
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ActiviteitDetailViewModel @Inject internal constructor(
    val activiteitEnDagGegevensRepository: ActiviteitEnDagGegevensRepository
): ViewModel() {

    private var _eventActiviteitSubmitted = MutableLiveData<Boolean>()
    val eventActiviteitSubmitted: LiveData<Boolean>
        get() = _eventActiviteitSubmitted

    private val _eventTitelVoiceInput = MutableLiveData<Boolean>()
    val eventTitelVoiceInput: LiveData<Boolean>
        get() = _eventTitelVoiceInput


    private val _eventNotitiesVoiceInput = MutableLiveData<Boolean>()
    val evenNotitiesVoiceInput: LiveData<Boolean>
        get() = _eventNotitiesVoiceInput

    val activiteit: LiveData<ActiviteitEnDagGegevensWeek>

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
        //TODO: replace with given activiteitId
        activiteit = activiteitEnDagGegevensRepository.getActiviteitEnDaggegevens(1)
        _eventActiviteitSubmitted.value = false
        _eventNotitiesVoiceInput.value = false
        _eventTitelVoiceInput.value = false
        _foutmelding.value = ""
        isMaandag =
            activiteit.value?.dagGegevens?.find { element -> element.dag == 0 }?.isActief == true
        isDinsdag =
            activiteit.value?.dagGegevens?.find { element -> element.dag == 1 }?.isActief == true
        isWoensdag =
            activiteit.value?.dagGegevens?.find { element -> element.dag == 2 }?.isActief == true
        isDonderdag =
            activiteit.value?.dagGegevens?.find { element -> element.dag == 3 }?.isActief == true
        isVrijdag =
            activiteit.value?.dagGegevens?.find { element -> element.dag == 4 }?.isActief == true
        isZaterdag =
            activiteit.value?.dagGegevens?.find { element -> element.dag == 5 }?.isActief == true
        isZondag =
            activiteit.value?.dagGegevens?.find { element -> element.dag == 6 }?.isActief == true
    }

    fun submitActiviteit() {
        if (activiteit.value?.activiteit?.titel.isNullOrEmpty()) {
            _foutmelding.value = "Vul de titel in."
        } else if (!(isZondag || isZaterdag || isVrijdag || isDonderdag || isWoensdag || isDinsdag || isMaandag)) {
            _foutmelding.value = "Vink minstens 1 dag aan"
        } else {
            viewModelScope.launch {
                updateDtb(activiteit.value!!)
                _eventActiviteitSubmitted.value = true
            }
        }
    }

    fun startVoiceInputTitel() {
        _eventTitelVoiceInput.value = true
    }

    fun startVoiceInputNotities() {
        _eventNotitiesVoiceInput.value = true
    }

    fun onSubmitComplete() {
        _eventActiviteitSubmitted.value = false
    }

    fun onFoutmeldingHandled() {
        _foutmelding.value = ""
    }

    fun onVoiceInputComplete() {
        _eventTitelVoiceInput.value = false
        _eventNotitiesVoiceInput.value = false
    }

    private suspend fun updateDtb(gegevens: ActiviteitEnDagGegevensWeek) {
        withContext(Dispatchers.IO) {
            activiteitEnDagGegevensRepository.updateActiviteit(gegevens.activiteit)
            (gegevens.dagGegevens.find { element -> element.dag == 0 })?.isActief = isMaandag
            (gegevens.dagGegevens.find { element -> element.dag == 1 })?.isActief = isDinsdag
            (gegevens.dagGegevens.find { element -> element.dag == 2 })?.isActief = isWoensdag
            (gegevens.dagGegevens.find { element -> element.dag == 3 })?.isActief = isDonderdag
            (gegevens.dagGegevens.find { element -> element.dag == 4 })?.isActief = isVrijdag
            (gegevens.dagGegevens.find { element -> element.dag == 5 })?.isActief = isZaterdag
            (gegevens.dagGegevens.find { element -> element.dag == 6 })?.isActief = isZondag
            //repository.updateDagGegevens(*(gegevens?.dagGegevens?.toTypedArray()))
        }
    }
}