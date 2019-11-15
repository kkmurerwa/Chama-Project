package  dev.ronnie.chama.groups

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.ronnie.chama.R
import dev.ronnie.chama.databinding.MembersListBinding
import dev.ronnie.chama.imageUtils.ImageActivity
import dev.ronnie.chama.models.DisplayUsers

class MembersRecyclerViewAdapter(var context: Context, var list: MutableList<DisplayUsers>) :
    RecyclerView.Adapter<MembersRecyclerViewAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val binding: MembersListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.members_list, parent, false
        )
        return MyViewHolder(binding)

    }

    override fun getItemCount(): Int {

        return list.size

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val displayUsers = list[position]
        holder.setData(displayUsers)

    }

    inner class MyViewHolder(var binding: MembersListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        var profileImage: String? = null
        var phoneNumber: String? = null

        init {
            binding.imageViewRe.setOnClickListener {
                if (!profileImage.isNullOrEmpty()) {

                    val intent = Intent(context, ImageActivity::class.java)
                    intent.putExtra("Image", profileImage)
                    context.startActivity(intent)
                }
            }

            binding.textViewPhone.setOnClickListener {
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:$phoneNumber")
                context.startActivity(intent)
            }
        }

        fun setData(displayUsers: DisplayUsers) {
            binding.memberModel = displayUsers

            if (!displayUsers.profile.isNullOrEmpty()) {
                Picasso.get()
                    .load(displayUsers.profile)
                    .fit()
                    .centerInside()
                    .into(binding.imageViewRe)
                profileImage = displayUsers.profile
            }

            if (!displayUsers.phone.isNullOrEmpty()) {
                phoneNumber = displayUsers.phone
            }

        }
    }
}
