package dev.ronnie.chama.groups

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.GroupListBinding
import dev.ronnie.chama.models.GroupUsers
import dev.ronnie.chama.models.Groups

class AllGroupRecyclerViewAdapter(var context: Context, private var groupList: List<Groups>?) :
    RecyclerView.Adapter<AllGroupRecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding: GroupListBinding = DataBindingUtil.inflate(
            LayoutInflater
                .from(parent.context), R.layout.group_list, parent, false
        )

        return MyViewHolder(binding)

    }

    override fun getItemCount(): Int {
        return groupList!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val group = groupList!![position]
        holder.setData(group)

    }

    inner class MyViewHolder(var binding: GroupListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var sendGroup: Groups? = null
        var isUserInTheGroup = false
        var isRequestSent = false

        init {

            binding.root.setOnClickListener {
                navigateIntoGroup()
            }
            binding.textViewJoin.setOnClickListener {
                GetGroups(null).sendJoinGroupRequest(sendGroup!!, AllGroupsFragment.viewModel)
            }
        }

        private fun navigateIntoGroup() {

            if (isUserInTheGroup) {
                AllGroupsFragment.viewModel.listener!!.navigate(sendGroup!!)
            } else {
                AllGroupsFragment.viewModel.listener!!.toast("You have not joined this group")
            }
        }


        fun setData(group: Groups) {

            val userList: MutableList<GroupUsers> = ArrayList()

            this.sendGroup = group
            binding.groupModel = group

            Log.d("Groups", "Group Names " + group.group_name)

            val reference = FirebaseDatabase.getInstance().reference
                .child("groups")
                .child(group.group_id)
                .child("users")
            reference.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    for (data in dataSnapshot.children) {

                        if (data.exists()) {

                            val user = data.getValue(GroupUsers::class.java)
                            if (user!!.userId.equals(FirebaseAuth.getInstance().currentUser!!.uid)) {
                                isUserInTheGroup = true
                            }

                            userList.add(user)
                        }
                    }
                    if (userList.size == 1) {
                        binding.textViewUserNumber.text = "${userList.size} member"
                    } else {
                        binding.textViewUserNumber.text = "${userList.size} members"
                    }


                }

            })
            val reference2 = FirebaseDatabase.getInstance().reference
                .child("groups")
                .child(group.group_id)
                .child("users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
            reference2.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(data: DataSnapshot) {

                    if (data.exists()) {
                        binding.textViewAlreadyJoined.visibility = View.VISIBLE
                        binding.textViewJoin.isEnabled = false
                        return
                    }

                }
            })
            val reference3 = FirebaseDatabase.getInstance().reference
                .child("groups")
                .child(group.group_id)
                .child("join_requests")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
            reference3.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(p0: DatabaseError) {

                }

                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        binding.textViewRequestSent.visibility = View.VISIBLE
                        binding.textViewJoin.isEnabled = false
                        isRequestSent = true

                    }

                    if (!isUserInTheGroup && !isRequestSent) {
                        binding.textViewJoin.visibility = View.VISIBLE
                    }

                }

            })

        }
    }
}