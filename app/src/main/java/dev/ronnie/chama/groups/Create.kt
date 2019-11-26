package dev.ronnie.chama.groups

import android.util.Log
import dev.ronnie.chama.models.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dev.ronnie.chama.models.Groups
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Create(val model: CreateGroupViewModel) {

    fun creatingGroup() {
        val activity = Activities()

        val withDrawals = WithDrawals()
        withDrawals.account = "Equity Bank"
        withDrawals.amount = "25000"
        withDrawals.date = "23-1-2006"

        val tasks = Tasks()
        tasks.action = "Harusi"
        tasks.date = "12-8-2019"
        tasks.taskName = "My Wedding"

        val deposits2 = Deposits()


        val withDrawals2 = WithDrawals()
        withDrawals2.account = "KCB"
        withDrawals2.amount = "25000"
        withDrawals2.date = "23-1-2006"

        val tasks2 = Tasks()
        tasks2.action = "Android Development"
        tasks2.date = "12-8-2019"
        tasks2.taskName = "My Wedding"

        val project = Projects()

        project.cost = "25100"
        project.leader = "Ronnie"
        project.name = "Building"
        project.runTime = "12 months"

        // val financials = Financials()

        model.listener!!.showProgressBar()

        val reference = FirebaseDatabase.getInstance().reference

        val groupId = reference
            .child("groups")
            .push().key

        val group = Groups()

        if (model.name.isNullOrEmpty()) {
            model.listener!!.toast("Name must not be empty")
            model.listener!!.hideProgressBar()
            return
        }
        if (model.name!![0].isWhitespace()) {
            model.listener!!.toast("Name must not start with a space")
            model.listener!!.hideProgressBar()
            return
        }
        group.group_name = model.name!!
        group.creator_id = FirebaseAuth.getInstance().currentUser!!.uid
        group.group_id = (groupId!!)

        reference
            .child("groups")
            .child(groupId)
            .setValue(group).addOnSuccessListener {
                val depositmainId = reference
                    .child("groups")
                    .child("activities")
                    .child("deposits")
                    .push().key
                val taskId = reference
                    .child("groups")
                    .child("activities")
                    .child("tasks")
                    .push().key

                val withId = reference
                    .child("groups")
                    .child("activities")
                    .child("withdrawals")
                    .push().key

                val with2Id = reference
                    .child("groups")
                    .child("activities")
                    .child("withdrawals")
                    .push().key

                val task2Id = reference
                    .child("groups")
                    .child("activities")
                    .child("tasks")
                    .push().key

                val depositId = reference
                    .child("groups")
                    .child("activities")
                    .child("deposits")
                    .push().key
                val deposit2Id = reference
                    .child("groups")
                    .child("activities")
                    .child("deposits")
                    .push().key
                val bankId = reference
                    .child("groups")
                    .child("financials")
                    .child("cash")
                    .child("bank")
                    .push().key
                val bank2Id = reference
                    .child("groups")
                    .child("financials")
                    .child("cash")
                    .child("bank")
                    .push().key
                val mpesaId = reference
                    .child("groups")
                    .child("financials")
                    .child("cash")
                    .child("mpesa")
                    .push().key
                val mpesa2Id = reference
                    .child("groups")
                    .child("financials")
                    .child("cash")
                    .child("mpesa")
                    .push().key

                val bank = Bank("EquityBank", "23456", bankId)
                val mpesa = Mpesa("Mpesa1", "52000", mpesaId)
                val bank2 = Bank("Coop", "2345600", bank2Id)
                val mpesa2 = Mpesa("Mpesa2", "520070", mpesa2Id)
                val projectId = reference
                    .child("groups")
                    .push().key

                reference.child("groups")
                    .child(groupId)
                    .child("financials")
                    .child("cash")
                    .child("bank")
                    .child(bankId!!)
                    .setValue(bank)
                reference.child("groups")
                    .child(groupId)
                    .child("financials")
                    .child("cash")
                    .child("bank")
                    .child(bank2Id!!)
                    .setValue(bank2)
                reference.child("groups")
                    .child(groupId)
                    .child("financials")
                    .child("cash")
                    .child("mpesa")
                    .child(mpesaId!!)
                    .setValue(mpesa)
                reference.child("groups")
                    .child(groupId)
                    .child("financials")
                    .child("cash")
                    .child("mpesa")
                    .child(mpesa2Id!!)
                    .setValue(mpesa2)

                reference.child("groups")
                    .child(groupId)
                    .child("projects")
                    .child(projectId!!)
                    .setValue(project)

                reference.child("groups")
                    .child(groupId)
                    .child("activities")
                    .setValue(activity)

                reference.child("groups")
                    .child(groupId)
                    .child("activities")
                    .child("deposits")
                    .child(depositId!!)
                    .setValue(deposits2)
                reference.child("groups")
                    .child(groupId)
                    .child("activities")
                    .child("deposits")
                    .child(deposit2Id!!)
                    .setValue(deposits2)

                reference.child("groups")
                    .child(groupId)
                    .child("activities")
                    .child("tasks")
                    .child(taskId!!)
                    .setValue(tasks)
                reference.child("groups")
                    .child(groupId)
                    .child("activities")
                    .child("tasks")
                    .child(task2Id!!)
                    .setValue(tasks2)
                reference.child("groups")
                    .child(groupId)
                    .child("activities")
                    .child("withdrawals")
                    .child(withId!!)
                    .setValue(withDrawals)
                reference.child("groups")
                    .child(groupId)
                    .child("activities")
                    .child("withdrawals")
                    .child(with2Id!!)
                    .setValue(withDrawals2)

                val project2 = Projects("Wedding", "Not Started", "Oscar", "2 months", "54023")
                val project2Id = reference
                    .child("groups")
                    .child("projects")
                    .push().key
                reference.child("groups")
                    .child(groupId)
                    .child("projects")
                    .child(project2Id!!)
                    .setValue(project2)
                val project3 = Projects("Wedding", "Completed", "Ronnie", "2 months", "54023")
                val project3Id = reference
                    .child("groups")
                    .child("projects")
                    .push().key
                reference.child("groups")
                    .child(groupId)
                    .child("projects")
                    .child(project3Id!!)
                    .setValue(project3)
                val project4 = Projects("Building", "Work In Progress", "Mary", "2 months", "54023")
                val project4Id = reference
                    .child("groups")
                    .child("projects")
                    .push().key
                reference.child("groups")
                    .child(groupId)
                    .child("projects")
                    .child(project4Id!!)
                    .setValue(project4)

                val inv1 = Investment("Farming", "Fixed interest", "5000", "Dec 23 2019")
                val inv2 = Investment("Business", "Shares", "45230", "Sept 25 2020")

                val inv1id = reference
                    .child("groups")
                    .child("financials")
                    .child("investments")
                    .push().key
                val inv2id = reference
                    .child("groups")
                    .child("financials")
                    .child("investments")
                    .push().key

                reference.child("groups")
                    .child(groupId)
                    .child("financials")
                    .child("investments")
                    .child(inv1id!!)
                    .setValue(inv1)
                reference.child("groups")
                    .child(groupId)
                    .child("financials")
                    .child("investments")
                    .child(inv2id!!)
                    .setValue(inv2)

                val chatId = reference
                    .child("groups")
                    .child("chatroom")
                    .push().key

                val groupUser = GroupUsers()

                val formatter = SimpleDateFormat("d MMM, yyyy", Locale.getDefault())

                try {
                    val dateString = formatter.format(Date())
                    groupUser.date_joined = dateString

                    Log.d("GetGroups", "DateString $dateString")
                } catch (e: ParseException) {
                    e.printStackTrace()
                }
                groupUser.userId = FirebaseAuth.getInstance().currentUser!!.uid
                reference.child("groups")
                    .child(group.group_id)
                    .child("users")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .setValue(groupUser)
                    .addOnSuccessListener {
                        Log.d(
                            "GetGroups", "User added"
                        )
                        reference.child("Users")
                            .child(FirebaseAuth.getInstance().currentUser!!.uid)
                            .child("my_groups")
                            .child(group.group_id)
                            .child("group_id")
                            .setValue(group.group_id)
                    }

                model.listener!!.hideProgressBar()
                model.listener!!.toast("Group Created")
                model.listener!!.notifyAdapter()

            }.addOnFailureListener {
                model.listener!!.toast(it.message.toString())
            }
    }
}