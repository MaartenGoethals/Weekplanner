package be.dekade.weekplanner

import androidx.lifecycle.*
import be.dekade.weekplanner.data.ActiviteitEnDagGegevensRepository
import be.dekade.weekplanner.data.ActiviteitEnDagGegevensWeek
import com.squareup.inject.assisted.Assisted
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import com.squareup.inject.assisted.AssistedInject

const val ACTIVITEITID_ARG = "ACTIVITEITID"

class ActiviteitDetailViewModel @AssistedInject constructor(
    val repository: ActiviteitEnDagGegevensRepository,
    val state: SavedStateHandle
): ViewModel() {
    val activiteitId = state.get<Long>("")
    private var _eventActiviteitSubmitted = MutableLiveData<Boolean>()
    val eventActiviteitSubmitted: LiveData<Boolean>
        get() = _eventActiviteitSubmitted

    private var _eventActiviteitDeleted = MutableLiveData<Boolean>()
    val eventActiviteitDeleted: LiveData<Boolean>
        get() = _eventActiviteitDeleted

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
        activiteit = activiteitId?.let { repository.getActiviteitEnDaggegevens(it) }!!
        _eventActiviteitSubmitted.value = false
        _eventActiviteitDeleted.value = false
        _eventNotitiesVoiceInput.value = false
        _eventTitelVoiceInput.value = false
        _foutmelding.value = ""
        isMaandag =
            activiteit.value?.dagGegevens?.find { element -> element.dag == Calendar.MONDAY }?.isActief == true
        isDinsdag =
            activiteit.value?.dagGegevens?.find { element -> element.dag == Calendar.TUESDAY }?.isActief == true
        isWoensdag =
            activiteit.value?.dagGegevens?.find { element -> element.dag == Calendar.WEDNESDAY }?.isActief == true
        isDonderdag =
            activiteit.value?.dagGegevens?.find { element -> element.dag == Calendar.THURSDAY }?.isActief == true
        isVrijdag =
            activiteit.value?.dagGegevens?.find { element -> element.dag == Calendar.FRIDAY }?.isActief == true
        isZaterdag =
            activiteit.value?.dagGegevens?.find { element -> element.dag == Calendar.SATURDAY }?.isActief == true
        isZondag =
            activiteit.value?.dagGegevens?.find { element -> element.dag == Calendar.SUNDAY }?.isActief == true
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

    fun deleteActiviteit(){
        activiteit.value?.activiteit?.let { repository.deleteActiviteit(it) }
        _eventActiviteitDeleted.value = true
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

    fun onDeleteComplete() {
        _eventActiviteitDeleted.value = false
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
            repository.updateActiviteit(gegevens.activiteit)
            (gegevens.dagGegevens.find { element -> element.dag == Calendar.MONDAY })?.isActief = isMaandag
            (gegevens.dagGegevens.find { element -> element.dag == Calendar.TUESDAY })?.isActief = isDinsdag
            (gegevens.dagGegevens.find { element -> element.dag == Calendar.WEDNESDAY })?.isActief = isWoensdag
            (gegevens.dagGegevens.find { element -> element.dag == Calendar.THURSDAY })?.isActief = isDonderdag
            (gegevens.dagGegevens.find { element -> element.dag == Calendar.FRIDAY })?.isActief = isVrijdag
            (gegevens.dagGegevens.find { element -> element.dag == Calendar.SATURDAY })?.isActief = isZaterdag
            (gegevens.dagGegevens.find { element -> element.dag == Calendar.SUNDAY })?.isActief = isZondag
            //repository.updateDagGegevens(*(gegevens?.dagGegevens?.toTypedArray()))
        }
    }
}