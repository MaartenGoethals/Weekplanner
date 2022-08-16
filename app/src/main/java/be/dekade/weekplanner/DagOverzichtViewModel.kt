package be.dekade.weekplanner

import androidx.lifecycle.*
import be.dekade.weekplanner.data.ActiviteitEnDagGegevensRepository
import be.dekade.weekplanner.data.DagGegevensData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DagOverzichtViewModel @Inject internal constructor(
    val activiteitEnDagGegevensRepository: ActiviteitEnDagGegevensRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    var activiteiten : LiveData<List<DagGegevensData>> = getWeekDag().switchMap {
        val _list = MutableLiveData<List<DagGegevensData>>()
        _list.value = activiteitEnDagGegevensRepository.getActiviteitenVoorDag(it)
        return@switchMap _list
    }

    private fun getWeekDag(): MutableLiveData<Int>{
        return savedStateHandle.getLiveData(WEEKDAG_KEY, DEFAULT_VALUE)
    }

    fun setWeekDag(dag: Int){
        savedStateHandle.set(WEEKDAG_KEY, dag)

    }

    fun reload(){
        activiteiten = getWeekDag().switchMap {
            val _list = MutableLiveData<List<DagGegevensData>>()
            _list.value = activiteitEnDagGegevensRepository.getActiviteitenVoorDag(it)
            return@switchMap _list
        }
    }

    companion object{
        private const val DEFAULT_VALUE = -1
        private const val WEEKDAG_KEY = "WEEKDAG_KEY"
    }
}