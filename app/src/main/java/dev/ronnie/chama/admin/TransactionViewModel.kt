package dev.ronnie.chama.admin

import androidx.lifecycle.ViewModel
import dev.ronnie.chama.models.Bank
import dev.ronnie.chama.models.Groups

class TransactionViewModel : ViewModel() {

    fun WithDrawBank(group: Groups, bank: Bank, reason: String?, amount: String?) {
        Transactions().withdrawBank(group, bank, reason, amount)
    }
    fun depositBank(group: Groups, bank: Bank, reason: String?, amount: String?){
        Transactions().depositBank(group, bank, reason, amount)
    }
}