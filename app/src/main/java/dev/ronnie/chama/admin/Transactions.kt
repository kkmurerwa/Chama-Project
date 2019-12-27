package dev.ronnie.chama.admin

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.ronnie.chama.models.*
import java.text.SimpleDateFormat
import java.util.*

class Transactions {
    fun depositBank(
        group: Groups,
        bank: Bank,
        reason: String?,
        amount: String?,
        model: TransactionViewModel
    ) {
        model.listener!!.startTransaction()
        val reference = FirebaseDatabase.getInstance().reference
        val depositId = reference
            .child("groups")
            .child("activities")
            .child("deposits")
            .push().key

        val deposits = Deposits(bank.bank_account, amount, timestamp, reason)
        reference.child("groups")
            .child(group.group_id)
            .child("activities")
            .child("deposits")
            .child(depositId!!)
            .setValue(deposits)
            .addOnSuccessListener {
                Log.d("Transactions", "Deposit Successfull")
                val reference1 = FirebaseDatabase.getInstance().reference
                val query = reference1
                    .child("groups")
                    .child(group.group_id)
                    .child("financials")
                    .child("cash")
                    .child("bank")
                    .orderByKey()
                    .equalTo(bank.bankId)
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {


                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        for (snapshot in p0.children) {
                            if (snapshot.exists()) {
                                val bankAmount = snapshot.getValue(Bank::class.java)!!.bank_amount
                                val bankId = snapshot.getValue(Bank::class.java)!!.bankId
                                val newAmount = bankAmount!!.toInt() + amount!!.toInt()
                                reference.child("groups")
                                    .child(group.group_id)
                                    .child("financials")
                                    .child("cash")
                                    .child("bank")
                                    .child(bankId!!)
                                    .child("bank_amount")
                                    .setValue(newAmount.toString())
                                    .addOnSuccessListener {
                                        Log.d("Transactions", "Bank Updated")
                                        model.listener!!.transactionComplete()
                                    }

                            }
                        }

                    }
                })
            }
    }

    private val timestamp: String
        get() {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            return sdf.format(Date())
        }

    fun withdrawBank(
        group: Groups,
        bank: Bank,
        reason: String?,
        amount: String?,
        model: TransactionViewModel
    ) {

        model.listener!!.startTransaction()
        val reference1 = FirebaseDatabase.getInstance().reference
        val query = reference1
            .child("groups")
            .child(group.group_id)
            .child("financials")
            .child("cash")
            .child("bank")
            .orderByKey()
            .equalTo(bank.bankId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {


            }

            override fun onDataChange(p0: DataSnapshot) {

                for (snapshot in p0.children) {
                    if (snapshot.exists()) {
                        val bankAmount = snapshot.getValue(Bank::class.java)!!.bank_amount
                        val bankId = snapshot.getValue(Bank::class.java)!!.bankId
                        val difference = bankAmount!!.toInt() - amount!!.toInt()

                        if (difference < 0) {
                            Log.d("Transactions", "You have less money")
                            model.listener!!.toast("You have insufficient fund in the Account")
                            model.listener!!.stopProgress()
                            return
                        } else if (difference > 0) {
                            val reference = FirebaseDatabase.getInstance().reference
                            val amountRemaining = bankAmount.toInt() - amount.toInt()
                            reference.child("groups")
                                .child(group.group_id)
                                .child("financials")
                                .child("cash")
                                .child("bank")
                                .child(bankId!!)
                                .child("bank_amount")
                                .setValue(amountRemaining.toString())
                                .addOnSuccessListener {
                                    val withId = reference
                                        .child("groups")
                                        .child("activities")
                                        .child("withdrawals")
                                        .push().key

                                    val withDrawals =
                                        WithDrawals(bank.bank_account, amount, timestamp, reason)
                                    reference.child("groups")
                                        .child(group.group_id)
                                        .child("activities")
                                        .child("withdrawals")
                                        .child(withId!!)
                                        .setValue(withDrawals)
                                        .addOnSuccessListener {
                                            Log.d("Transactions", "Withdraw successfull")
                                            model.listener!!.transactionComplete()
                                        }
                                }
                        }
                    }
                }

            }
        })

    }

    fun depositMpesa(
        group: Groups,
        mpesa: Mpesa,
        reason: String?,
        amount: String?,
        model: TransactionViewModel
    ) {
        model.listener!!.startTransaction()
        val reference = FirebaseDatabase.getInstance().reference
        val depositId = reference
            .child("groups")
            .child("activities")
            .child("deposits")
            .push().key

        val deposits = Deposits(mpesa.mpesa_account, amount, timestamp, reason)
        reference.child("groups")
            .child(group.group_id)
            .child("activities")
            .child("deposits")
            .child(depositId!!)
            .setValue(deposits)
            .addOnSuccessListener {
                Log.d("Transactions", "Deposit Successfull")
                val reference1 = FirebaseDatabase.getInstance().reference
                val query = reference1
                    .child("groups")
                    .child(group.group_id)
                    .child("financials")
                    .child("cash")
                    .child("mpesa")
                    .orderByKey()
                    .equalTo(mpesa.mpesaId)
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {


                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        for (snapshot in p0.children) {
                            if (snapshot.exists()) {
                                val mpesaAmount =
                                    snapshot.getValue(Mpesa::class.java)!!.mpesa_amount
                                val mpesaId = snapshot.getValue(Mpesa::class.java)!!.mpesaId
                                val newAmount = mpesaAmount!!.toInt() + amount!!.toInt()
                                reference.child("groups")
                                    .child(group.group_id)
                                    .child("financials")
                                    .child("cash")
                                    .child("mpesa")
                                    .child(mpesaId!!)
                                    .child("mpesa_amount")
                                    .setValue(newAmount.toString())
                                    .addOnSuccessListener {
                                        Log.d("Transactions", "Mpesa Updated")
                                        model.listener!!.transactionComplete()
                                    }

                            }
                        }

                    }
                })
            }

    }

    fun withdrawMpesa(
        group: Groups,
        mpesa: Mpesa,
        reason: String?,
        amount: String?,
        model: TransactionViewModel
    ) {

        model.listener!!.startTransaction()
        val reference1 = FirebaseDatabase.getInstance().reference
        val query = reference1
            .child("groups")
            .child(group.group_id)
            .child("financials")
            .child("cash")
            .child("mpesa")
            .orderByKey()
            .equalTo(mpesa.mpesaId)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {


            }

            override fun onDataChange(p0: DataSnapshot) {

                for (snapshot in p0.children) {
                    if (snapshot.exists()) {
                        val mpesaAmount = snapshot.getValue(Mpesa::class.java)!!.mpesa_amount
                        val mpesaId = snapshot.getValue(Mpesa::class.java)!!.mpesaId
                        val difference = mpesaAmount!!.toInt() - amount!!.toInt()

                        if (difference < 0) {
                            Log.d("Transactions", "You have less money")
                            model.listener!!.toast("You have insufficient fund in the Account")
                            model.listener!!.stopProgress()
                            return
                        } else if (difference > 0) {
                            val reference = FirebaseDatabase.getInstance().reference
                            val amountRemaining = mpesaAmount.toInt() - amount.toInt()
                            reference.child("groups")
                                .child(group.group_id)
                                .child("financials")
                                .child("cash")
                                .child("mpesa")
                                .child(mpesaId!!)
                                .child("mpesa_amount")
                                .setValue(amountRemaining.toString())
                                .addOnSuccessListener {
                                    val withId = reference
                                        .child("groups")
                                        .child("activities")
                                        .child("withdrawals")
                                        .push().key

                                    val withDrawals =
                                        WithDrawals(mpesa.mpesa_account, amount, timestamp, reason)
                                    reference.child("groups")
                                        .child(group.group_id)
                                        .child("activities")
                                        .child("withdrawals")
                                        .child(withId!!)
                                        .setValue(withDrawals)
                                        .addOnSuccessListener {
                                            Log.d("Transactions", "Withdraw successfull")
                                            model.listener!!.transactionComplete()
                                        }
                                }
                        }
                    }
                }

            }
        })

    }
}