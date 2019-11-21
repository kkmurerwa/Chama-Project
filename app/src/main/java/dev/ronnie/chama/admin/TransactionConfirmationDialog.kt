package dev.ronnie.chama.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import dev.ronnie.chama.R
import dev.ronnie.chama.models.Bank
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.models.Mpesa
import kotlinx.android.synthetic.main.layout_transaction_confirmation.view.*


class TransactionConfirmationDialog : DialogFragment() {

    var views: View? = null
    lateinit var group: Groups
    private var bank: Bank? = null
    private var mpesa: Mpesa? = null
    private var reason: String? = null
    private var amount: String? = null
    lateinit var textConfirm: TextView
    lateinit var viewModel: TransactionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        views = inflater.inflate(R.layout.layout_transaction_confirmation, container, false)
        viewModel = ViewModelProviders.of(this)[TransactionViewModel::class.java]

        textConfirm = views!!.textViewConfirm

        val bundle = arguments
        if (bundle?.containsKey("bank")!! && bundle.containsKey("type-deposit")) {
            group = bundle.getParcelable("group") as Groups
            bank = bundle.getSerializable("bank") as Bank
            reason = bundle.getString("reason")
            amount = bundle.getString("amount")
            startDepositingBank(group, bank!!, reason, amount)
        } else if (bundle.containsKey("bank") && bundle.containsKey("type-withdraw")) {
            group = bundle.getParcelable("group") as Groups
            bank = bundle.getSerializable("bank") as Bank
            reason = bundle.getString("reason")
            amount = bundle.getString("amount")
            startWithdrawBank(group, bank!!, reason, amount)
        }


        return views
    }

    private fun startWithdrawBank(group: Groups, bank: Bank, reason: String?, amount: String?) {
        textConfirm.text =
            getString(R.string.confirm_transaction, "withdraw", amount, "from", bank.bank_account)
        views!!.textConfirm.setOnClickListener {
            viewModel.WithDrawBank(group, bank, reason, amount)
        }
        views!!.textCancel.setOnClickListener {
            dialog!!.dismiss()
        }
    }

    private fun startDepositingBank(group: Groups, bank: Bank, reason: String?, amount: String?) {
        textConfirm.text =
            getString(R.string.confirm_transaction, "Deposit", amount, "to", bank.bank_account)
        views!!.textConfirm.setOnClickListener {
            viewModel.depositBank(group, bank, reason, amount)
        }
        views!!.textCancel.setOnClickListener {
            dialog!!.dismiss()
        }

    }


}
