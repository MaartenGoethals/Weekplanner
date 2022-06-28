package be.dekade.weekplanner

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.dekade.weekplanner.data.Activiteit

class NieuweActiviteitViewModel : ViewModel() {

    val activiteit = MutableLiveData<Activiteit>()

    init {
        activiteit.value = Activiteit("", "", 8, 0, 15, false, false, false, false,false,false, false, -1,-1,true)
    }
}