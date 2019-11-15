package dev.ronnie.chama.admin

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.RequestListBinding
import dev.ronnie.chama.models.GroupUsers
import dev.ronnie.chama.models.Groups
import dev.ronnie.chama.models.User
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class RequestJoinRecyclerView(var context: Context, var list: MutableList<User>) :
    RecyclerView.Adapter<RequestJoinRecyclerView.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding: RequestListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.request_list,
            parent,
            false
        )

        return MyViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val requester = list[position]
        holder.setData(requester)
    }

    inner class MyViewHolder(var binding: RequestListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val group = AdminActivity.group
        private var user: User? = null

        init {
            binding.textViewAccept.setOnClickListener {
                addUserToGroup(group, user!!)
            }
            binding.textViewDeny.setOnClickListener {

            }
        }

        fun setData(requester: User) {
            user = requester
            binding.textViewRequestContact.text = requester.phone
            binding.textViewRequestName.text = requester.fname + requester.sname
        }

        private fun addUserToGroup(groups: Groups, user: User) {

            var isUserAdded = false
            val reference = FirebaseDatabase.getInstance().reference

            val ref = FirebaseDatabase.getInstance().reference
                .child("groups")
                .child(groups.group_id)
                .child("users")
            ref.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                    return
                }

                override fun onDataChange(data: DataSnapshot) {

                    for (singleSnapshot in data.children) {
                        if (user.user_id == singleSnapshot.getValue(
                                GroupUsers::class.java
                            )!!.userId
                        ) {
                            Log.d("User", "User Already Added")
                            isUserAdded = true
                            return
                        }

                    }
                    if (!isUserAdded) {

                        val groupUser = GroupUsers()

                        val formatter = SimpleDateFormat("d MMM, yyyy", Locale.getDefault())

                        try {
                            val dateString = formatter.format(Date())
                            groupUser.date_joined = dateString

                            Log.d("GetGroups", "DateString $dateString")
                        } catch (e: ParseException) {
                            e.printStackTrace()
                        }
                        groupUser.userId = user.user_id

                        reference.child("groups")
                            .child(groups.group_id)
                            .child("users")
                            .child(user.user_id!!)
                            .setValue(groupUser)
                            .addOnSuccessListener {
                                Log.d(
                                    "GetGroups", "User added"
                                )
                                reference.child("Users")
                                    .child(user.user_id!!)
                                    .child("my_groups")
                                    .child(groups.group_id)
                                    .child("group_id")
                                    .setValue(groups.group_id)
                                    .addOnSuccessListener {
                                        reference
                                            .child("groups")
                                            .child(group.group_id)
                                            .child("join_requests")
                                            .child(user.user_id!!)
                                            .removeValue()
                                            .addOnSuccessListener {
                                                Toast.makeText(
                                                    context,
                                                    "${user.fname} added to the Group",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                binding.textViewAccept.isEnabled = false

                                            }
                                    }
                            }

                    }

                }

            })


        }
    }
}
