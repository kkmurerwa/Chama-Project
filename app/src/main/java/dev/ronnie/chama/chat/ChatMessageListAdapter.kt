package dev.ronnie.chama.chat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import dev.ronnie.chama.R
import dev.ronnie.chama.models.ChatMessage
import java.util.*


class ChatMessageListAdapter(
    private val mContext: Context,
    private val mLayoutResource: Int,
    objects: List<ChatMessage>
) : ArrayAdapter<ChatMessage>(mContext, mLayoutResource, objects) {


    class ViewHolder {

        internal var message: TextView? = null
        internal var mProfileImage: CircleImageView? = null
        internal var name: TextView? = null

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        val holder: ViewHolder

        if (convertView == null) {
            val inflater = LayoutInflater.from(mContext)
            convertView = inflater.inflate(mLayoutResource, parent, false)
            holder = ViewHolder()

            holder.message = convertView!!.findViewById(R.id.message_body)
            holder.mProfileImage = convertView.findViewById(R.id.img_thumbnail)
            holder.name = convertView.findViewById(R.id.message_sender)

            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
            holder.message!!.text = ""
        }

        try {
            holder.message!!.text = getItem(position)!!.message
            val androidColors =
                context.resources.getIntArray(R.array.androidcolors)
            val randomAndroidColor = androidColors[Random().nextInt(androidColors.size)]
            holder.name!!.setTextColor(randomAndroidColor)
            holder.name!!.text = getItem(position)!!.name

            if (holder.mProfileImage!!.tag == null || holder.mProfileImage!!.tag != getItem(position)!!.profile_image) {

                if (getItem(position)!!.profile_image != "") {
                    Picasso.get()
                        .load(getItem(position)!!.profile_image)
                        .fit()
                        .centerInside()
                        .into(holder.mProfileImage)
                }
                holder.mProfileImage!!.tag = getItem(position)!!.profile_image

            }


        } catch (e: NullPointerException) {
            Log.e(TAG, "getView: NullPointerException: ", e.cause)
        }

        return convertView
    }

    companion object {

        private val TAG = "ChatMessageListAdapter"
    }

}
