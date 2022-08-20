package be.dekade.weekplanner

import android.app.Application
import androidx.lifecycle.*
import be.dekade.weekplanner.data.ActiviteitEnDagGegevensRepository
import be.dekade.weekplanner.data.ActiviteitEnDagGegevensWeek
import be.dekade.weekplanner.helpers.NotificationHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class ActiviteitDetailViewModel @Inject internal constructor(
    val repository: ActiviteitEnDagGegevensRepository,
    val state: SavedStateHandle,
    application: Application
): AndroidViewModel(application) {
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

    val isMaandag: MutableLiveData<Boolean>
    val isDinsdag: MutableLiveData<Boolean>
    val isWoensdag: MutableLiveData<Boolean>
    val isDonderdag: MutableLiveData<Boolean>
    val isVrijdag: MutableLiveData<Boolean>
    val isZaterdag: MutableLiveData<Boolean>
    val isZondag: MutableLiveData<Boolean>

    init {
        isMaandag = MutableLiveData<Boolean>()
        isDinsdag = MutableLiveData<Boolean>()
        isWoensdag = MutableLiveData<Boolean>()
        isDonderdag = MutableLiveData<Boolean>()
        isVrijdag = MutableLiveData<Boolean>()
        isZaterdag = MutableLiveData<Boolean>()
        isZondag = MutableLiveData<Boolean>()
        val activiteitId = state.get<Long>("activiteitId")
        activiteit = activiteitId?.let { repository.getActiviteitEnDaggegevens(it) }!!
        _eventActiviteitSubmitted.value = false
        _eventActiviteitDeleted.value = false
        _eventNotitiesVoiceInput.value = false
        _eventTitelVoiceInput.value = false
        _foutmelding.value = ""
    }

    fun activiteitChanged(){
        isMaandag.value =
            activiteit.value?.dagGegevens?.find { element -> element.dag == Calendar.MONDAY }?.isActief == true
        isDinsdag.value =
            activiteit.value?.dagGegevens?.find { element -> element.dag == Calendar.TUESDAY }?.isActief == true
        isWoensdag.value =
            activiteit.value?.dagGegevens?.find { element -> element.dag == Calendar.WEDNESDAY }?.isActief == true
        isDonderdag.value =
            activiteit.value?.dagGegevens?.find { element -> element.dag == Calendar.THURSDAY }?.isActief == true
        isVrijdag.value =
            activiteit.value?.dagGegevens?.find { element -> element.dag == Calendar.FRIDAY }?.isActief == true
        isZaterdag.value =
            activiteit.value?.dagGegevens?.find { element -> element.dag == Calendar.SATURDAY }?.isActief == true
        isZondag.value =
            activiteit.value?.dagGegevens?.find { element -> element.dag == Calendar.SUNDAY }?.isActief == true
    }

    fun submitActiviteit() {
        if (activiteit.value?.activiteit?.titel.isNullOrEmpty()) {
            _foutmelding.value = "Vul de titel in."
        } else if (!(isZondag.value == true || isZaterdag.value ==true || isVrijdag.value ==true || isDonderdag.value ==true || isWoensdag.value ==true || isDinsdag.value ==true || isMaandag.value ==true)) {
            _foutmelding.value = "Vink minstens 1 dag aan"
        } else {
            viewModelScope.launch {
                updateDtb(activiteit.value!!)
                _eventActiviteitSubmitted.value = true
            }
        }
    }

    fun deleteActiviteit(){
        viewModelScope.launch {
            activiteit.value?.activiteit?.let { repository.deleteActiviteit(it) }
        }
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
            (gegevens.dagGegevens.find { element -> element.dag == Calendar.MONDAY })?.isActief = isMaandag.value ==true
            (gegevens.dagGegevens.find { element -> element.dag == Calendar.TUESDAY })?.isActief = isDinsdag.value ==true
            (gegevens.dagGegevens.find { element -> element.dag == Calendar.WEDNESDAY })?.isActief = isWoensdag.value ==true
            (gegevens.dagGegevens.find { element -> element.dag == Calendar.THURSDAY })?.isActief = isDonderdag.value ==true
            (gegevens.dagGegevens.find { element -> element.dag == Calendar.FRIDAY })?.isActief = isVrijdag.value ==true
            (gegevens.dagGegevens.find { element -> element.dag == Calendar.SATURDAY })?.isActief = isZaterdag.value ==true
            (gegevens.dagGegevens.find { element -> element.dag == Calendar.SUNDAY })?.isActief = isZondag.value ==true
            repository.updateActiviteit(gegevens.activiteit)
            repository.updateDagGegevens(gegevens.dagGegevens)
            NotificationHelper.setNotifications(gegevens, getApplication())
        }
    }
}