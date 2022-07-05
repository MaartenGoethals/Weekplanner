package be.dekade.weekplanner

import androidx.lifecycle.*
import be.dekade.weekplanner.data.Activiteit
import be.dekade.weekplanner.data.ActiviteitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DagOverzichtViewModel @Inject internal constructor(
    activiteitRepository: ActiviteitRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    val activiteiten : LiveData<List<Activiteit>> = getWeekDag().switchMap{
        if (it== DEFAULT_VALUE){
            activiteitRepository.getActiviteiten()
        }else{
            activiteitRepository.getActiviteitenVoorDag(it)
        }
    }

    private fun getWeekDag(): MutableLiveData<Int>{
        return savedStateHandle.getLiveData(WEEKDAG_KEY, DEFAULT_VALUE)
    }

    fun setWeekDag(dag: Int){
        savedStateHandle.set(WEEKDAG_KEY, dag)
    }

    companion object{
        private const val DEFAULT_VALUE = -1
        private const val WEEKDAG_KEY = "WEEKDAG_KEY"
    }
}