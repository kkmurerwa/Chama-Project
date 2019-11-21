package dev.ronnie.chama.admin

import com.google.firebase.database.FirebaseDatabase
import dev.ronnie.chama.models.Bank
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.models.Mpesa

class AddAccounts {

    fun addBankAccount(model: AddAccountViewModel, group: Groups, bankAccountName: String) {

        if (bankAccountName.isEmpty()) {
            return
        }
        if (bankAccountName[0].isWhitespace()) {
            return
        }

        val reference = FirebaseDatabase.getInstance().reference
        val bankId = reference
            .child("groups")
            .child("financials")
            .child("cash")
            .child("bank")
            .push().key

        val bank = Bank(bankAccountName, "0", bankId)

        reference.child("groups")
            .child(group.group_id)
            .child("financials")
            .child("cash")
            .child("bank")
            .child(bankId!!)
            .setValue(bank)
            .addOnCompleteListener {
                model.listener!!.setViewsAfter()

            }

    }

    fun addMpesaAccount(model: AddAccountViewModel, group: Groups, mpesaAccountName: String) {

        if (mpesaAccountName.isEmpty()) {
            return
        }
        if (mpesaAccountName[0].isWhitespace()) {
            return
        }

        val reference = FirebaseDatabase.getInstance().reference
        val mpesaId = reference
            .child("groups")
            .child("financials")
            .child("cash")
            .child("mpesa")
            .push().key

        val mpesa = Mpesa(mpesaAccountName, "0", mpesaId)

        reference.child("groups")
            .child(group.group_id)
            .child("financials")
            .child("cash")
            .child("mpesa")
            .child(mpesaId!!)
            .setValue(mpesa)
            .addOnCompleteListener {
                model.listener!!.setViewsAfter()
            }

    }
}