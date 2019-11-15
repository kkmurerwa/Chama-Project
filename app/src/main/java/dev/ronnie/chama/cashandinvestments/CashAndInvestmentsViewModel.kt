package dev.ronnie.chama.cashandinvestments

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.ronnie.chama.data.FireBaseData
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.models.Investment

class CashAndInvestmentsViewModel : ViewModel() {

    var listener: CashAndInvesmentsListener? = null


    fun openCash(view: View) {

        listener!!.openActivities(1)

    }

    fun openInvestMents(view: View) {

        listener!!.openActivities(2)
    }

    fun getInvestments(group: Groups): LiveData<MutableList<Investment>> {
        return FireBaseData().getInvestment(group)
    }

}