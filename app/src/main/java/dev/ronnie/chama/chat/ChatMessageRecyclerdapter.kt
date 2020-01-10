package dev.ronnie.chama.chat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import dev.ronnie.chama.R
import dev.ronnie.chama.models.ChatMessage
import kotlinx.android.synthetic.main.message_list.view.*
import kotlinx.android.synthetic.main.my_message_list.view.*
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class ChatMessageRecyclerdapter(var context: Context, var list: List<ChatMessage>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val view: View

        return when (viewType) {
            0 -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.my_message_list, parent, false)
                SendViewHolder(view)
            }
            else -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.message_list, parent, false)
                ReceiveViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {

        return list.size

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = list[position]

        when (holder.itemViewType) {
            0 -> (holder as SendViewHolder?)!!.bind(
                message
            )
            1 -> (holder as ReceiveViewHolder?)!!.bind(
                message
            )
        }

    }

    override fun getItemViewType(position: Int): Int {

        val message = list[position]

        return if (message.user_id == FirebaseAuth.getInstance().currentUser!!.uid) {
            0
        } else {
            1
        }

    }

    inner class SendViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: ChatMessage) {

            itemView.text_message_body.text = message.message


            var dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val date: Date
            try {
                date = dateFormat.parse(message.timestamp)
                dateFormat = SimpleDateFormat("hh:mm aaa")
                val formatedDate: String = dateFormat.format(date)
                itemView.my_message_time.text = formatedDate
                Log.d("DateNew", formatedDate)
            } catch (e1: ParseException) {
                e1.printStackTrace()
            }


        }

    }

    inner class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(message: ChatMessage) {
            itemView.message_body.text = message.message

            val androidColors =
                context.resources.getIntArray(R.array.androidcolors)
            val randomAndroidColor = androidColors[Random().nextInt(androidColors.size)]
            itemView.message_sender.setTextColor(randomAndroidColor)
            itemView.message_sender.text = message.name


            var dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
            val date: Date
            try {
                date = dateFormat.parse(message.timestamp)
                dateFormat = SimpleDateFormat("hh:mm aaa")
                val formatedDate: String = dateFormat.format(date)
                itemView.text_message_time.text = formatedDate
                Log.d("DateNew", formatedDate)
            } catch (e1: ParseException) {
                e1.printStackTrace()
            }


            if (itemView.img_thumbnail.tag == null || itemView.img_thumbnail.tag != message.profile_image) {

                if (message.profile_image != "") {
                    Picasso.get()
                        .load(message.profile_image)
                        .fit()
                        .centerInside()
                        .into(itemView.img_thumbnail)

                    itemView.img_thumbnail.tag = message.profile_image
                }
            }

        }

    }

}