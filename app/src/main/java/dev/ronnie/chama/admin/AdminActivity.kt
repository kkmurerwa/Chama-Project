package dev.ronnie.chama.admin

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.ActivityAdminBinding
import dev.ronnie.chama.models.Groups
import kotlinx.android.synthetic.main.activity_admin.*


class AdminActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

    companion object {
        lateinit var group: Groups
        var isAdminActivityRunning = false
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
                onBackPressed()
            }
        }
        val intent = intent
        group = intent.getParcelableExtra("group")

        init()
        cardTransactions.setOnClickListener {
            showTransactions(cardTransactions)
        }
        cardOthers.setOnClickListener {
            showOthers(cardOthers)
        }
    }

    private fun init() {

        viewModel.getRequestList(group).observe(this, Observer {
            val adapter = RequestJoinRecyclerView(this, it)
        })

    }

    private fun showTransactions(v: View?) {
        val popup = PopupMenu(this, v!!)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.transaction_menu)
        popup.show()
    }

    private fun showOthers(v: View?) {
        val popup = PopupMenu(this, v!!)
        popup.setOnMenuItemClickListener(this)
        popup.inflate(R.menu.others_menu)
        popup.show()
    }


    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.bank -> {
                showBankAccount()
                true
            }
            R.id.mpesa -> {
                showMpesaAccount()
                true
            }
            R.id.investmentMenu -> {
                Toast.makeText(this, "Investment clicked", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.tasksMenu -> {
                Toast.makeText(this, "Tasks clicked", Toast.LENGTH_SHORT).show()
                true
            }

            else -> false
        }
    }

    private fun showBankAccount() {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("BankDialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        val dialog = BankAccountsFragment()
        val bundle = Bundle()
        bundle.putParcelable("group", group)
        dialog.arguments = bundle
        dialog.show(ft, "BankDialog")
    }

    private fun showMpesaAccount() {
        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("MpesaDialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        val dialog = MpesaAccountsFragment()
        val bundle = Bundle()
        bundle.putParcelable("group", group)
        dialog.arguments = bundle
        dialog.show(ft, "MpesaDialog")
    }

    override fun onStart() {
        super.onStart()

        isAdminActivityRunning = true
    }

    override fun onStop() {
        super.onStop()

        isAdminActivityRunning = false
    }

}
