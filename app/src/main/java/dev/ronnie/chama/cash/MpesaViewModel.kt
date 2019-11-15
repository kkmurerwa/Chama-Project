package dev.ronnie.chama.cash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.ronnie.chama.data.FireBaseData
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.models.Mpesa

class MpesaViewModel : ViewModel() {

    var listener: MpesaListener? = null

    fun getMpesa(group: Groups): LiveData<MutableList<Mpesa>> {
        return FireBaseData().getMpesa(group)
    }
}