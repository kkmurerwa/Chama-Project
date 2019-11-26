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
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


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

            var dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val date: Date
            try {
                date = dateFormat.parse(deposit.date)
                dateFormat = SimpleDateFormat("d MMM, yyyy 'at' HH:mm aaa")
                val formatedDate: String = dateFormat.format(date)
                deposit.date = formatedDate
                Log.d("DateNew", formatedDate)
            } catch (e1: ParseException) {
                e1.printStackTrace()
            }

            activityAdapterBinding.deposit = deposit


        }


    }
}