package dev.ronnie.chama.cash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.ronnie.chama.data.FireBaseData
import dev.ronnie.chama.models.Bank
import dev.ronnie.chama.models.Groups

class BankViewModel : ViewModel() {

    var listener: BankListener? = null

    fun getBank(group: Groups): LiveData<MutableList<Bank>> {
        return FireBaseData().getBank(group)
    }
}