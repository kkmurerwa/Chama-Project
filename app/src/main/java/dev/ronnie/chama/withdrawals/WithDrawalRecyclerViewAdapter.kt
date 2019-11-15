package dev.ronnie.chama.withdrawals

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.WithdrawListBinding
import dev.ronnie.chama.models.WithDrawals

class WithDrawalRecyclerViewAdapter(var context: Context, var list: List<WithDrawals>) :
    RecyclerView.Adapter<WithDrawalRecyclerViewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val activityAdapterBinding = DataBindingUtil.inflate<WithdrawListBinding>(
            LayoutInflater
                .from(parent.context), R.layout.withdraw_list, parent, false
        )
        return MyViewHolder(activityAdapterBinding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val withDrawals = list[position]
        holder.setData(withDrawals)

    }

    inner class MyViewHolder(private var activityAdapterBinding: WithdrawListBinding) :
        RecyclerView.ViewHolder(activityAdapterBinding.root) {


        fun setData(withDrawals: WithDrawals) {
            withDrawals.apply {

                this.amount = "-Shs ${this.amount}"

                Log.d(
                    "DepositsAdapter", "Adapter Found: "
                            + withDrawals.account!! + " " + withDrawals.account!!
                )
                activityAdapterBinding.withdraw = withDrawals

            }
        }

    }
}
