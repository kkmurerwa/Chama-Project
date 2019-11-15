package dev.ronnie.chama.withdrawals

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.ActivityWithdrawalsBinding
import dev.ronnie.chama.models.Groups
import kotlinx.android.synthetic.main.activity_withdrawals.*


class WithDrawalsActivity : AppCompatActivity(), WithDrawalListener {

    lateinit var group: Groups
    lateinit var binding: ActivityWithdrawalsBinding
    lateinit var viewModel: WithDrawalsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_withdrawals)
        viewModel = ViewModelProviders.of(this)[WithDrawalsViewModel::class.java]
        binding.withDrawModel = viewModel
        viewModel.listener = this

        if (toolbar != null) {
            setSupportActionBar(toolbar as Toolbar?)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            title = "Withdrawals"

            (toolbar as Toolbar).setNavigationOnClickListener {
                finish()
            }
        }

        init()

    }

    private fun init() {
        val intent = intent
        group = intent.getParcelableExtra("group")
        viewModel.getWithDrawals(group).observe(this, Observer {

            val adapter = WithDrawalRecyclerViewAdapter(this, it)
            binding.withDrawRecyclerview.layoutManager = LinearLayoutManager(this)
            binding.withDrawRecyclerview.adapter = adapter
        })

    }
}