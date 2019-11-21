package dev.ronnie.chama.admin

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.ronnie.chama.data.FireBaseData
import dev.ronnie.chama.models.Bank
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.models.Mpesa

class AddAccountViewModel : ViewModel() {

    var listener: AddAcountListener? = null

    fun getBank(group: Groups): LiveData<MutableList<Bank>> {
        return FireBaseData().getBank(group)
    }
    fun getMpesa(group: Groups): LiveData<MutableList<Mpesa>> {
        return FireBaseData().getMpesa(group)
    }


    fun addBankAccount(group: Groups, name: String) {
        AddAccounts().addBankAccount(this, group, name)
    }
    fun addMpesaAccount(group: Groups, name: String) {
        AddAccounts().addMpesaAccount(this, group, name)
    }
}