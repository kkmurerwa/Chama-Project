package dev.ronnie.chama.investment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.ronnie.chama.data.FireBaseData
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.models.Investment

class InvestmentViewModel : ViewModel() {

    var listener: InvestmentListener? = null

    fun getInvestments(group: Groups): LiveData<MutableList<Investment>> {

        return FireBaseData().getInvestment(group)
    }
}