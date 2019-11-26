package dev.ronnie.chama.admin

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.ronnie.chama.data.FireBaseData
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.models.User

class ConfirmRequestViewModel : ViewModel() {

    var listener: RequestListener? = null

    fun getRequestList(group: Groups): MutableLiveData<MutableList<User>> {
        return  FireBaseData().getJoinRequest(group,this)
    }
}