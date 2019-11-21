package dev.ronnie.chama.profile

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

            val urlTask = it.storage.downloadUrl
            while (!urlTask.isSuccessful);
            url = urlTask.result.toString()
            model.downloadUrl = url
            FirebaseDatabase.getInstance().reference
                .child("Users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("profile_image")
                .setValue(url)
                .addOnCompleteListener {
                    model.listener!!.savePofilePicToPrefs(url!!)
                    model.listener!!.enableSaveButton()
                    model.listener!!.progressBarGone()
                    model.listener!!.toast("Picture Updated Successfully")
                }

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

        user.fname = model.fname
        user.sname = model.sname
        user.phone = model.phone
        user.user_id = FirebaseAuth.getInstance().currentUser!!.uid
        model.listener!!.progressBarVisible()
        FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .child("fname")
            .setValue(user.fname)
            .addOnCompleteListener {
                FirebaseDatabase.getInstance().reference
                    .child("Users")
                    .child(FirebaseAuth.getInstance().currentUser!!.uid)
                    .child("sname")
                    .setValue(user.sname)
                    .addOnCompleteListener {
                        FirebaseDatabase.getInstance().reference
                            .child("Users")
                            .child(FirebaseAuth.getInstance().currentUser!!.uid)
                            .child("phone")
                            .setValue(user.phone)
                            .addOnCompleteListener {
                                model.listener!!.progressBarGone()
                                model.listener!!.toast("Profile Updated")

                            }
                    }

            }

    }


}

