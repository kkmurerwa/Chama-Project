package dev.ronnie.chama.deposits

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.ronnie.chama.data.FireBaseData
import dev.ronnie.chama.models.Deposits
import dev.ronnie.chama.models.Groups

class DepositsViewModel : ViewModel() {

    lateinit var listener: DepositsListener


    fun getDeposits(group: Groups): LiveData<MutableList<Deposits>> {

        return FireBaseData().getDeposit(group)
    }
}