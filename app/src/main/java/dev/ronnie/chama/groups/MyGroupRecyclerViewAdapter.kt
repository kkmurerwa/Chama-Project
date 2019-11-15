package  dev.ronnie.chama.groups

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.MyGroupListBinding
import dev.ronnie.chama.models.GroupUsers
import dev.ronnie.chama.models.Groups

class MyGroupRecyclerViewAdapter(var context: Context, var list: List<Groups>?) :
    RecyclerView.Adapter<MyGroupRecyclerViewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: MyGroupListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.my_group_list,
            parent,
            false
        )

        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val group = list!![position]
        holder.setData(group)
    }


    inner class MyViewHolder(var binding: MyGroupListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var groupSend: Groups? = null


        init {
            binding.root.setOnClickListener {
                MyGroupsFragment.viewModel.listener!!.navigate(groupSend!!)
            }
        }

        fun setData(group: Groups) {
            groupSend = group
            binding.textViewGroupName.text = group.group_name
            val reference = FirebaseDatabase.getInstance().reference
                .child("groups")
                .child(group.group_id)
                .child("users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
            reference.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(data: DataSnapshot) {


                    if (data.exists()) {

                        val dateJoined =
                            data.getValue(GroupUsers::class.java)!!.date_joined

                        binding.textViewJoinedDate.text = "Joined on $dateJoined"
                    }

                }

                override fun onCancelled(data: DatabaseError) {

                }


            })


        }
    }
}