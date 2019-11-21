package dev.ronnie.chama.admin

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import dev.ronnie.chama.R
import dev.ronnie.chama.models.Bank
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.models.Mpesa
import kotlinx.android.synthetic.main.layout_transaction.*


class TransactionActivity : AppCompatActivity() {

    var titleString: String = ""
    var bank: Bank? = null
    var group: Groups? = null
    var mpesa: Mpesa? = null
    var isintentBank = false
    var isIntentMpesa = false
    private var reason: String? = null
    private var amount: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_transaction)

        val intent = intent

        if (intent.hasExtra("mpesa")) {
            mpesa = intent.getSerializableExtra("mpesa") as Mpesa
            titleString = mpesa!!.mpesa_account!!
            isIntentMpesa = true
            textViewAccount.text = getString(R.string.transaction_name, "Mpesa")
        }
        if (intent.hasExtra("bank")) {
            bank = intent.getSerializableExtra("bank") as Bank
            titleString = bank!!.bank_account!!
            isintentBank = true
            textViewAccount.text = getString(R.string.transaction_name, "Bank")
        }
        if (intent.hasExtra("group")) {
            group = intent.getParcelableExtra("group") as Groups
            Log.d("Transaction", "Group name ${group!!.group_name}")
        }
        if (toolbar != null) {
            setSupportActionBar(toolbar as Toolbar?)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setHomeButtonEnabled(true)
            title = titleString

            (toolbar as Toolbar).setNavigationOnClickListener {
                onBackPressed()
            }

        }
        btn_transact.setOnClickListener {
            when {
                radio_deposit.isChecked -> {
                    if (isIntentMpesa) {
                        depositMpesa()

                    } else if (isintentBank) {

                        depositBank()
                    }
                    Log.d("Transaction", "Radio Selection Deposit Checked")
                }
                radio_withdraw.isChecked -> {
                    if (isIntentMpesa) {
                        withdrawMpesa()

                    } else if (isintentBank) {
                        withdrawBank()
                        Log.d("Transaction", "Radio Selection Withdraw Checked")
                    }
                   // Log.d("Transaction", "Radio Selection Withdraw Checked")
                }
                else -> {
                    Log.d("Transaction", "Radio Selection You Haven't selected")
                }
            }

        }
    }

    private fun withdrawBank() {
        amount = input_amount.text.toString()
        reason = input_reason.text.toString()

        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("confirmDialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        val dialog = TransactionConfirmationDialog()
        val bundle = Bundle()
        bundle.putParcelable("group", group)
        bundle.putSerializable("bank", bank)
        bundle.putString("amount", amount)
        bundle.putString("reason", reason)
        bundle.putString("type-withdraw", "withdraw")
        dialog.arguments = bundle
        dialog.show(ft, "confirmDialog")

    }

    private fun withdrawMpesa() {


    }

    private fun depositMpesa() {

    }

    private fun depositBank() {

        amount = input_amount.text.toString()
        reason = input_reason.text.toString()

        val ft = supportFragmentManager.beginTransaction()
        val prev = supportFragmentManager.findFragmentByTag("confirmDialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)

        val dialog = TransactionConfirmationDialog()
        val bundle = Bundle()
        bundle.putParcelable("group", group)
        bundle.putSerializable("bank", bank)
        bundle.putString("amount", amount)
        bundle.putString("reason", reason)
        bundle.putString("type-deposit", "deposit")
        dialog.arguments = bundle
        dialog.show(ft, "confirmDialog")

    }

}