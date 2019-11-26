package dev.ronnie.chama.admin

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.LayoutTransactionBinding
import dev.ronnie.chama.models.Bank
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.models.Mpesa
import kotlinx.android.synthetic.main.layout_transaction.*
import kotlinx.android.synthetic.main.layout_transaction_confirmation.view.*


class TransactionActivity : AppCompatActivity(), TransactionListener {

    var titleString: String = ""
    var bank: Bank? = null
    var group: Groups? = null
    var mpesa: Mpesa? = null
    var isintentBank = false
    var isIntentMpesa = false
    private var reason: String? = null
    private var amount: String? = null
    lateinit var progress: ProgressBar
    lateinit var viewModel: TransactionViewModel
    lateinit var binding: LayoutTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.layout_transaction)
        viewModel = ViewModelProviders.of(this)[TransactionViewModel::class.java]
        viewModel.listener = this

        init()
        btn_transact.setOnClickListener {
            handleClick()

        }
    }

    private fun handleClick() {
        when {
            radio_deposit.isChecked -> {
                if (isIntentMpesa) {
                    transactMpesa("deposit")
                } else if (isintentBank) {

                    transactBank("deposit")
                }
                Log.d("Transaction", "Radio Selection Deposit Checked")
            }
            radio_withdraw.isChecked -> {
                if (isIntentMpesa) {
                    transactMpesa("withdraw")

                } else if (isintentBank) {
                    transactBank("withdraw")
                    Log.d("Transaction", "Radio Selection Withdraw Checked")
                }
            }
            else -> {
                Log.d("Transaction", "Radio Selection You Haven't selected")
            }
        }
    }

    private fun init() {
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
    }


    private fun transactMpesa(s: String) {


        amount = input_amount.text.toString()
        reason = input_reason.text.toString()

        if (reason.isNullOrEmpty()) {

            toastErrors("Please Enter the Message for this Transaction")
            return
        }
        if (reason!![0].isWhitespace()) {
            toastErrors("Message should not start with a space")
            return
        }
        if (amount.isNullOrEmpty()) {
            toastErrors("Please Enter the Amount to Transact")

            return
        }

        var confirmationText: String? = null
        if (s == "deposit") {
            confirmationText = getString(
                R.string.confirm_transaction,
                "Deposit",
                amount,
                "to",
                mpesa!!.mpesa_account
            )
        } else if (s == "withdraw") {
            confirmationText = getString(
                R.string.confirm_transaction,
                "Withdraw",
                amount,
                "from",
                mpesa!!.mpesa_account
            )
        }

        val builder = AlertDialog.Builder(this)
        val view: View = View.inflate(this, R.layout.layout_transaction_confirmation, null)

        val confirmationTextView = view.textViewConfirm
        confirmationTextView.text = confirmationText

        builder.setView(view)

        builder.setPositiveButton(
            "CONFIRM"
        ) { dialog, _ ->
            if (s == "deposit") {
                viewModel.depositMpesa(group!!, mpesa!!, reason!!, amount!!)
            } else if (s == "withdraw") {
                viewModel.withdrawMpesa(group!!, mpesa!!, reason!!, amount!!)
            }
            dialog.dismiss()
        }
        builder.setNegativeButton(
            "CANCEL"
        ) { dialog, _ ->
            dialog.cancel()
        }

        builder.show()


    }


    private fun transactBank(s: String) {

        amount = input_amount.text.toString()
        reason = input_reason.text.toString()

        if (reason.isNullOrEmpty()) {

            toastErrors("Please Enter the Message for this Transaction")
            return
        }
        if (reason!![0].isWhitespace()) {
            toastErrors("Message should not start with a space")
            return
        }
        if (amount.isNullOrEmpty()) {
            toastErrors("Please Enter the Amount to Transact")

            return
        }

        var confirmationText: String? = null
        if (s == "deposit") {
            confirmationText = getString(
                R.string.confirm_transaction,
                "Deposit",
                amount,
                "to",
                bank!!.bank_account
            )
        } else if (s == "withdraw") {
            confirmationText = getString(
                R.string.confirm_transaction,
                "Withdraw",
                amount,
                "from",
                bank!!.bank_account
            )
        }

        val builder = AlertDialog.Builder(this)
        val view: View = View.inflate(this, R.layout.layout_transaction_confirmation, null)

        val confirmationTextView = view.textViewConfirm
        confirmationTextView.text = confirmationText

        builder.setView(view)

        builder.setPositiveButton(
            "CONFIRM"
        ) { dialog, _ ->
            if (s == "deposit") {
                viewModel.depositBank(group!!, bank!!, reason, amount)
            } else if (s == "withdraw") {
                viewModel.withdrawBank(group!!, bank!!, reason, amount)
            }
            dialog.dismiss()
        }
        builder.setNegativeButton(
            "CANCEL"
        ) { dialog, _ ->
            dialog.cancel()
        }

        builder.show()


    }


    override fun toast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    override fun transactionComplete() {

        binding.transactionProgress.visibility = View.GONE
        binding.inputAmount.setText("")
        binding.inputReason.setText("")
        val toast =
            Toast.makeText(this, "Transaction Completed Successfully", Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()


    }

    override fun startTransaction() {

        binding.transactionProgress.visibility = View.VISIBLE


    }

    private fun toastErrors(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()


    }

    override fun stopProgress() {
        binding.transactionProgress.visibility = View.GONE
    }


}