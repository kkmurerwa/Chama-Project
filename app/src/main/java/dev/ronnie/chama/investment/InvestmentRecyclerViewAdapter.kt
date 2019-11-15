package dev.ronnie.chama.investment

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView

import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.InvestmentListBinding
import dev.ronnie.chama.models.Investment

class InvestmentRecyclerViewAdapter(var context: Context, var listInve: MutableList<Investment>) :
    RecyclerView.Adapter<InvestmentRecyclerViewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: InvestmentListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.investment_list, parent, false
        )

        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listInve.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val investment = listInve[position]

        holder.binding.inve = investment
    }

    inner class MyViewHolder(var binding: InvestmentListBinding) :
        RecyclerView.ViewHolder(binding.root)
}