package dev.ronnie.chama.groups

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.ronnie.chama.models.Groups

class AllGroupsViewModel : ViewModel() {
    var listener: GroupListener? = null


    fun init(): LiveData<ArrayList<Groups>> {
        return GetGroups(this).getGroups()
    }

}