package dev.ronnie.chama.cash

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.MpesaListBinding
import dev.ronnie.chama.models.Mpesa

class MpesaRecyclerViewAdapter(var context: Context, var mpesaList: MutableList<Mpesa>) :
    RecyclerView.Adapter<MpesaRecyclerViewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding: MpesaListBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.mpesa_list,
                parent,
                false
            )
        return MyViewHolder(binding)

    }

    override fun getItemCount(): Int {

        return mpesaList.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val mpesa = mpesaList[position]

        mpesa.let {
            it.mpesa_amount = "Shs ${it.mpesa_amount}"
            holder.binding.mpesa = it
        }


    }

    inner class MyViewHolder(var binding: MpesaListBinding) : RecyclerView.ViewHolder(binding.root)
}