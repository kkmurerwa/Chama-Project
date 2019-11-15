package dev.ronnie.chama.profile

import android.util.Log

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import dev.ronnie.chama.models.User

class UploadProfilePicture(var model: ProfileViewModel) {

    var url: String? = null

    fun upload(bytes: ByteArray) {

        val storageReference = FirebaseStorage.getInstance().reference
            .child(
                "images/users/${FirebaseAuth.getInstance().currentUser!!.uid}/profile_image"
            )

        val metadata = StorageMetadata.Builder()
            .setContentType("image/jpg")
            .setContentLanguage("en")
            .build()

        storageReference.putBytes(bytes, metadata).addOnSuccessListener {

            model.listener!!.progressBarGone()
            model.listener!!.toast("Uploaded Successfully")

            val urlTask = it.storage.downloadUrl
            while (!urlTask.isSuccessful);
            url = urlTask.result.toString()
            model.downloadUrl = url

            Log.d("Upload", "DownloadUrl $url")

            model.listener!!.enableSaveButton()

        }.addOnFailureListener {
            model.listener!!.progressBarGone()
            model.listener!!.toast(it.message.toString())

        }.addOnProgressListener {
            model.listener!!.progressBarVisible()
            model.listener!!.disableSaveButton()
        }

    }


    fun saveProfile() {

        val user = User()

        if (model.sname.isNullOrEmpty() || model.fname.isNullOrEmpty() || model.phone.isNullOrEmpty()) {
            model.listener!!.toast("Fields cannot be empty")
            return
        }
        if (model.downloadUrl.isNullOrEmpty()) {

            model.downloadUrl = Profile.profile.toString()

            Log.d("Uploads", "DownloadUrlParsed ${model.downloadUrl}")

        }

        user.fname = model.fname
        user.sname = model.sname
        user.phone = model.phone
        user.profile_image = model.downloadUrl
        user.user_id = FirebaseAuth.getInstance().currentUser!!.uid
        model.listener!!.progressBarVisible()
        FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .setValue(user)
            .addOnCompleteListener {
                model.listener!!.progressBarGone()
                if (it.isSuccessful) {
                    model.listener!!.toast("User Updated")
                }
            }.addOnFailureListener {
                model.listener!!.toast("Something Went Wrong")
                model.listener!!.progressBarGone()


            }


    }

}