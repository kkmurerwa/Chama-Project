package dev.ronnie.chama.groups

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dev.ronnie.chama.models.GroupUsers
import dev.ronnie.chama.models.Groups
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CreateGroup(val model: CreateGroupViewModel) {

    fun creatingGroup() {
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