package  dev.ronnie.chama.groups

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.models.Mygroups
import java.util.*
import kotlin.collections.ArrayList

class GetGroups(val model: AllGroupsViewModel?) {

    private val reference = FirebaseDatabase.getInstance().reference
    var allGroupsList: ArrayList<Groups>? = ArrayList()
    var islistEmpty: Boolean = false
    val allGroupsLiveData = MutableLiveData<ArrayList<Groups>>()

    companion object {
        var myGroupsList: ArrayList<Groups>? = ArrayList()
    }

    var myGroupsLiveData = MutableLiveData<ArrayList<Groups>>()


    fun getGroups(): LiveData<ArrayList<Groups>> {

        val query = reference.child("groups").orderByKey()
        query.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                allGroupsList!!.clear()
                if (dataSnapshot.exists()) {
                    for (singleSnapshot in dataSnapshot.children) {

                        Log.d(
                            "GetGroups", "onDataChange: found group: "
                                    + singleSnapshot.value
                        )
                        try {
                            if (singleSnapshot.exists()) {

                                val groups = Groups()
                                val objectMap = singleSnapshot.value as HashMap<*, *>?
                                groups.group_id =
                                    objectMap!!["group_id"]!!.toString()
                                groups.group_name =
                                    objectMap["group_name"]!!.toString()
                                groups.creator_id =
                                    objectMap["creator_id"]!!.toString()
                                allGroupsList!!.add(groups)
                                allGroupsLiveData.value = allGroupsList
                            }
                        } catch (e: NullPointerException) {
                            Log.e(
                                "GetGroups",
                                "onDataChange: NullPointerException: " + e.message.toString()
                            )
                        }
                    }
                } else if (!dataSnapshot.exists()) {
                    AllGroupsFragment.progressbar.get()!!.visibility = View.GONE
                    AllGroupsFragment.textViewEmpty.get()!!.visibility = View.VISIBLE
                    Log.d(
                        "GetGroups",
                        "list empty $islistEmpty"
                    )
                    return
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })
        return allGroupsLiveData
    }


    fun getMyGroups(): LiveData<ArrayList<Groups>> {

        val query = reference.child("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("my_groups")
        query.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                myGroupsList!!.clear()

                if (dataSnapshot.exists()) {
                    for (singleSnapshot in dataSnapshot.children) {

                        val groupId = singleSnapshot.getValue(Mygroups::class.java)!!.group_id

                        val query2 = reference.child("groups").orderByKey()
                            .equalTo(groupId)
                        query2.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(p0: DatabaseError) {

                            }

                            override fun onDataChange(dataSnapshot2: DataSnapshot) {
                                for (singleSnapshot2 in dataSnapshot2.children) {
                                    Log.d(
                                        "GetGroups", "onDataChange: found My Groups: "
                                                + singleSnapshot2.value
                                    )
                                    try {
                                        if (singleSnapshot2.exists()) {

                                            val groups = Groups()
                                            val objectMap = singleSnapshot2.value as HashMap<*, *>?
                                            groups.group_id =
                                                objectMap!!["group_id"]!!.toString()
                                            groups.group_name =
                                                objectMap["group_name"]!!.toString()
                                            groups.creator_id =
                                                objectMap["creator_id"]!!.toString()
                                            myGroupsList!!.add(groups)
                                            myGroupsLiveData.value = myGroupsList
                                        }
                                    } catch (e: NullPointerException) {
                                        Log.e(
                                            "GetGroups",
                                            "onDataChange: NullPointerException: " + e.message.toString()
                                        )
                                    }
                                }
                            }
                        })
                    }
                } else if (!dataSnapshot.exists()) {
                    MyGroupsFragment.progressbar.get()!!.visibility = View.GONE
                    MyGroupsFragment.textViewEmpty.get()!!.visibility = View.VISIBLE
                    return
                }
            }

            override fun onCancelled(dataSnapshot: DatabaseError) {

            }
        })


        return myGroupsLiveData

    }

    fun sendJoinGroupRequest(group: Groups, models: AllGroupsViewModel) {

        val reference = FirebaseDatabase.getInstance().reference
            .child("groups")
            .child(group.group_id)
            .child("join_requests")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("user_id")
            .setValue(FirebaseAuth.getInstance().currentUser!!.uid)
        reference.addOnSuccessListener {
            Log.d("Join", "Request Sent")

        }.addOnFailureListener {

        }

    }


}


