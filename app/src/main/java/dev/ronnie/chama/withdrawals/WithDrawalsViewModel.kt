package dev.ronnie.chama.withdrawals

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.ronnie.chama.data.FireBaseData
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.models.WithDrawals

class WithDrawalsViewModel : ViewModel() {


    var listener: WithDrawalListener? = null

    fun getWithDrawals(group: Groups): LiveData<MutableList<WithDrawals>> {

        return FireBaseData().getWithDrawals(group)

    }
}