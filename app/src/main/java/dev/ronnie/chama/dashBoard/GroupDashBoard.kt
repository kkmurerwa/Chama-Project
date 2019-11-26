package dev.ronnie.chama.dashBoard

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.ActivityDashboardBinding
import dev.ronnie.chama.deposits.DepositsActivity
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.tasks.TasksActivity
import dev.ronnie.chama.withdrawals.WithDrawalsActivity
import kotlinx.android.synthetic.main.activity_dashboard.*


class GroupDashBoard : AppCompatActivity(), DashBoardListener {


    lateinit var viewModel: DashBoardViewModel
    lateinit var binding: ActivityDashboardBinding
    lateinit var group: Groups


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_dashboard
        )
        viewModel = ViewModelProviders.of(this)[DashBoardViewModel::class.java]
        binding.dashBoardModel = viewModel
        viewModel.listener = this

        if (toolbar != null) {
            setSupportActionBar(toolbar as Toolbar?)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            title = "Chama"

            (toolbar as Toolbar).setNavigationOnClickListener {
                finish()
            }
        }

        init()

    }

    private fun init() {
        val intent = intent
        group = intent.getParcelableExtra("group")

        viewModel.getDeposits(group).observe(this, Observer {

            var depositAmount = 0

            for (i in it) {

                val money = i.amount!!.substring(5)

                Log.d("DashBoard", "Deposits Amount $money")
                depositAmount += money.toInt()
            }

            textViewDepositsAmount.text = getString(R.string.depositAmount, depositAmount)
        })

        viewModel.getWithdrawals(group).observe(this, Observer {

            var withdrawAmount = 0
            for (i in it) {
                val money = i.amount!!.substring(5)
                Log.d("DashBoard", "Deposits Amount $money")
                withdrawAmount += money.toInt()

            }
            textViewWithdrawalAmount.text = getString(R.string.withdrawAmount, withdrawAmount)
        })

        viewModel.getTasks(group).observe(this, Observer {

            textViewTaskAmount.text = getString(R.string.tasks, it.size)
        })

    }

    override fun navigate(code: Int) {
        when (code) {
            1 -> {
                val intent = Intent(this, DepositsActivity::class.java)
                intent.putExtra("group", group)
                startActivity(intent)
            }
            2 -> {
                val intent = Intent(this, WithDrawalsActivity::class.java)
                intent.putExtra("group", group)
                startActivity(intent)
            }
            3 -> {
                val intent = Intent(this, TasksActivity::class.java)
                intent.putExtra("group", group)
                startActivity(intent)
            }

        }

    }
}