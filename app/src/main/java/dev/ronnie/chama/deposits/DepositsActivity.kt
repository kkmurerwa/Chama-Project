package dev.ronnie.chama.deposits

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.ActivityDepositsBinding
import dev.ronnie.chama.models.Deposits
import dev.ronnie.chama.models.Groups
import kotlinx.android.synthetic.main.activity_deposits.*

class DepositsActivity : AppCompatActivity(), DepositsListener {


    lateinit var group: Groups
    lateinit var binding: ActivityDepositsBinding
    lateinit var viewModel: DepositsViewModel
    lateinit var depositsList: ArrayList<Deposits>
    lateinit var adapter: DepositsRecyclerViewAdapater

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_deposits)
        viewModel = ViewModelProviders.of(this)[DepositsViewModel::class.java]
        binding.depositsModel = viewModel
        viewModel.listener = this

        if (toolbar != null) {
            setSupportActionBar(toolbar as Toolbar?)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            title = "Deposits"

            (toolbar as Toolbar).setNavigationOnClickListener {
               onBackPressed()
            }

        }


        init()

    }

    private fun init() {
        val intent = intent
        group = intent.getParcelableExtra("group")
        viewModel.getDeposits(group).observe(this, Observer {
            depositsList = it as ArrayList<Deposits>

            adapter = DepositsRecyclerViewAdapater(this, it)
            binding.depositsRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.depositsRecyclerView.adapter = adapter
            adapter.notifyDataSetChanged()

        })

    }


}