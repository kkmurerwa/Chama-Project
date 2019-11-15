package dev.ronnie.chama.investment

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.ActivityInvestmentsBinding
import dev.ronnie.chama.models.Groups
import kotlinx.android.synthetic.main.activity_investments.*

class InvestmentsActivity : AppCompatActivity(), InvestmentListener {
    lateinit var group: Groups
    lateinit var viewModel: InvestmentViewModel
    lateinit var binding: ActivityInvestmentsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_investments)
        viewModel = ViewModelProviders.of(this)[InvestmentViewModel::class.java]
        binding.inveModel = viewModel
        viewModel.listener = this

        if (toolbar != null) {
            setSupportActionBar(toolbar as Toolbar?)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            title = "Investments"

            (toolbar as Toolbar).setNavigationOnClickListener {
                finish()
            }

        }
        init()

    }

    private fun init() {
        val intent = intent
        group = intent.getParcelableExtra("group")

        viewModel.getInvestments(group).observe(this, Observer {
            val adapter = InvestmentRecyclerViewAdapter(this, it)
            binding.inveRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.inveRecyclerView.adapter = adapter

        })
    }
}