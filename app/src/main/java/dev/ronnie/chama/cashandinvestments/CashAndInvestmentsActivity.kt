package dev.ronnie.chama.cashandinvestments

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dev.ronnie.chama.R
import dev.ronnie.chama.cash.BanksAndMpesaActivity
import dev.ronnie.chama.databinding.CashandinvestmentsBinding
import dev.ronnie.chama.investment.InvestmentsActivity
import dev.ronnie.chama.models.Groups
import kotlinx.android.synthetic.main.cashandinvestments.*


class CashAndInvestmentsActivity : AppCompatActivity(), CashAndInvesmentsListener {

    lateinit var viewModel: CashAndInvestmentsViewModel
    lateinit var binding: CashandinvestmentsBinding
    lateinit var group: Groups


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.cashandinvestments)
        viewModel = ViewModelProviders.of(this)[CashAndInvestmentsViewModel::class.java]
        binding.cashModel = viewModel
        viewModel.listener = this

        if (toolbar != null) {
            setSupportActionBar(toolbar as Toolbar?)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            title = "Cash and Investments"

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

            binding.investmentsNumber.text = "${it.size} investments"

        })
    }

    override fun openActivities(code: Int) {
        when (code) {
            1 -> {
                val intent = Intent(this, BanksAndMpesaActivity::class.java)
                intent.putExtra("group", group)
                startActivity(intent)
            }
            2 -> {
                val intent = Intent(this, InvestmentsActivity::class.java)
                intent.putExtra("group", group)
                startActivity(intent)
            }
        }
    }
}