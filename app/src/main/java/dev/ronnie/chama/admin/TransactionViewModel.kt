package dev.ronnie.chama.admin

import androidx.lifecycle.ViewModel
import dev.ronnie.chama.models.Bank
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.models.Mpesa

class TransactionViewModel : ViewModel() {

    var listener: TransactionListener? = null

    fun depositBank(
        group: Groups,
        bank: Bank,
        reason: String?,
        amount: String?
    ) {
        Transactions().depositBank(group, bank, reason, amount, this)
    }

    fun withdrawBank(group: Groups, bank: Bank, reason: String?, amount: String?) {

        Transactions().withdrawBank(group, bank, reason, amount, this)


    }

    fun depositMpesa(group: Groups, mpesa: Mpesa, reason: String, amount: String) {
        Transactions().depositMpesa(group, mpesa, reason, amount, this)

    }

    fun withdrawMpesa(group: Groups, mpesa: Mpesa, reason: String, amount: String) {
        Transactions().withdrawMpesa(group, mpesa, reason, amount, this)

    }


}