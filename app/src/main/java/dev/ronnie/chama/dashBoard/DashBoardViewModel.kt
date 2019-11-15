package dev.ronnie.chama.dashBoard

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import dev.ronnie.chama.data.FireBaseData
import dev.ronnie.chama.models.Deposits
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.models.Tasks
import dev.ronnie.chama.models.WithDrawals


class DashBoardViewModel : ViewModel() {

    var listener: DashBoardListener? = null

    fun openDeposits(view: View) {

        listener!!.navigate(1)

    }

    fun openWithDrawals(view: View) {
        listener!!.navigate(2)

    }

    fun openTasks(view: View) {
        listener!!.navigate(3)

    }

    fun getDeposits(group: Groups): LiveData<MutableList<Deposits>> {

        return FireBaseData().getDeposit(group)
    }

    fun getWithdrawals(group: Groups): LiveData<MutableList<WithDrawals>> {

        return FireBaseData().getWithDrawals(group)
    }
    fun getTasks(group: Groups): LiveData<MutableList<Tasks>> {

        return FireBaseData().getTasks(group)
    }


}