package be.dekade.weekplanner

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import be.dekade.weekplanner.data.Activiteit

class DagOverzichtViewModel : ViewModel() {
    val activiteiten : LiveData<MutableList<Activiteit>> = MutableLiveData(mutableListOf<Activiteit>())

    init {
        //TODO: get real data
        activiteiten.value!!.add(Activiteit(
            0,
            "Voorbeeld",
            "Dit is een voorbeeldactiviteit. %n je hoeft hier niks te doen.",
            8,
            0,
            true,
            true,
            true,
            true,
            true,
            true,
            true,
            -1,
            -1,
            true,
            false
        ))
        Log.d("APP", "init() called")
    }
}