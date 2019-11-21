package dev.ronnie.chama.models

import android.os.Parcel
import android.os.Parcelable

class Groups(
    var group_name: String = "",
    var creator_id: String = "",
    var group_id: String = "",
    var activities: Activities? = null,
    var projects: Projects? = null,
    var financials: Financials? = null,
    var users: List<String>? = null,
    var chatroom_messages: List<ChatMessage>? = null
) : Parcelable {

    protected constructor(`in`: Parcel) : this() {
        group_name = `in`.readString()!!
        creator_id = `in`.readString()!!
        group_id = `in`.readString()!!
        users = `in`.createStringArrayList()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(group_name)
        dest.writeString(creator_id)
        dest.writeString(group_id)
        dest.writeStringList(users)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Groups> =
            object : Parcelable.Creator<Groups> {
                override fun createFromParcel(`in`: Parcel): Groups? {
                    return Groups(`in`)
                }

                override fun newArray(size: Int): Array<Groups?> {
                    return arrayOfNulls(size)
                }
            }
    }
}