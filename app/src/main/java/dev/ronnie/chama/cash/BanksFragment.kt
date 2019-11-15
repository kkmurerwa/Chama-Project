package dev.ronnie.chama.cash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.FragmentBanksBinding
import dev.ronnie.chama.models.Groups

class BanksFragment : Fragment(), BankListener {

    lateinit var binding: FragmentBanksBinding
    lateinit var viewModel: BankViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_banks, container, false)
        viewModel = ViewModelProviders.of(this)[BankViewModel::class.java]
        viewModel.listener = this
        binding.bank = viewModel


        val bundle = arguments
        if (bundle != null) {
            val group = bundle.getParcelable("group") as Groups
            init(group)
            Log.d("Mpesa", "Bank Found ${group.group_name}")
        } else {
            Log.d("Mpesa", "Bank Group Null")
        }
        return binding.root
    }

    private fun init(group: Groups) {

        viewModel.getBank(group).observe(this, Observer {
            val adapter = BankRecyclerViewAdapter(context!!, it)
            binding.bankRecyclerView.layoutManager = LinearLayoutManager(context)
            binding.bankRecyclerView.adapter = adapter
        })

    }

}
