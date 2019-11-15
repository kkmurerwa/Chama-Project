package dev.ronnie.chama.profile

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    var downloadUrl: String? = null


    fun sendInput(bytes: ByteArray) {

        Log.d("ProfileViewModel", "Received")

        UploadProfilePicture(this).upload(bytes)
    }

    var sname: String? = null
    var fname: String? = null
    var phone: String? = null
    var listener: ProfileListener? = null

    fun getProfile() {

        val displayProfile = Profile(this).getProfile()
        listener!!.displayProfile(displayProfile)
    }

    fun setPhoto(view: View) {

        listener!!.showDialog()

    }

    fun saveProfile(view: View) {

        UploadProfilePicture(this).saveProfile()

    }

    fun openPicture(view: View) {
        listener!!.openPicture()
    }


}