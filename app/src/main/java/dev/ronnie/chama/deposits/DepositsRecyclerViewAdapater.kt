package dev.ronnie.chama.deposits

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.DepositListBinding
import dev.ronnie.chama.models.Deposits

class DepositsRecyclerViewAdapater(var context: Context, var deposeitList: List<Deposits>) :
    RecyclerView.Adapter<DepositsRecyclerViewAdapater.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val activityAdapterBinding: DepositListBinding = DataBindingUtil.inflate(
            LayoutInflater
                .from(parent.context), R.layout.deposit_list, parent, false
        )
        return MyViewHolder(activityAdapterBinding)
    }

    override fun getItemCount(): Int {
        return deposeitList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val deposit = deposeitList[position]
        holder.setData(deposit)

    }


    inner class MyViewHolder(var activityAdapterBinding: DepositListBinding) :
        RecyclerView.ViewHolder(activityAdapterBinding.root) {

        fun setData(deposit: Deposits) {
            deposit.let {

                it.amount = "+Shs ${deposit.amount}"
                Log.d(
                    "DepositsAdapter", "Adapter Found: "
                            + deposit.account!! + " " + deposit.account!!
                )
                activityAdapterBinding.deposit = it

            }
        }


    }
}