package dev.ronnie.chama.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.ActivityAdminBinding
import dev.ronnie.chama.models.Groups
import kotlinx.android.synthetic.main.activity_admin.*

class AdminActivity : AppCompatActivity() {

    companion object {
        lateinit var group: Groups
    }


    lateinit var mServerKey: String
    lateinit var viewModel: AdminViewModel
    lateinit var binding: ActivityAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_admin)
        viewModel = ViewModelProviders.of(this)[AdminViewModel::class.java]
        binding.model = viewModel

        if (toolbar != null) {
            setSupportActionBar(toolbar as Toolbar?)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            title = "Admin"

            (toolbar as Toolbar).setNavigationOnClickListener {
                finish()
            }
        }
        val intent = intent
        group = intent.getParcelableExtra("group")

        init()


    }

    private fun init() {

        viewModel.getRequestList(group).observe(this, Observer {

            val adapter = RequestJoinRecyclerView(this, it)
            binding.requestRecyclerView.layoutManager = LinearLayoutManager(this)
            binding.requestRecyclerView.adapter = adapter
        })

    }


}
