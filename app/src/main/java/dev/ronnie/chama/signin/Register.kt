package dev.ronnie.chama.signin

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId
import dev.ronnie.chama.models.User


class Register(private var model: SignUpViewModel) {

    fun startRegistering(email: String, password: String) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    model.listener!!.progressBarGone()
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { result: Task<AuthResult> ->
                            if (result.isSuccessful) {
                                verifyEmail()

                            }
                        }.addOnFailureListener {
                            model.listener!!.toast("Something Went Wrong")
                        }
                    Log.d("Register", "Sign Success")
                    saveUser(email, password)



                }
            }.addOnFailureListener {

                Log.d("Register", it.toString())
                model.listener!!.progressBarGone()
                model.listener!!.toast(it.message.toString())
            }


    }

    fun saveUser(email: String, password: String) {
        val user = User()
        user.fname = email.substring(0, email.indexOf("@"))
        user.phone = ""
        user.profile_image = ""
        user.user_id = FirebaseAuth.getInstance().currentUser!!.uid
        FirebaseDatabase.getInstance().reference
            .child("Users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .setValue(user)
            .addOnCompleteListener {

            }.addOnFailureListener {
                model.listener!!.toast("Something Went Wrong")
                FirebaseAuth.getInstance().signOut()
                model.listener!!.goToLogIn()

            }

    }


    private fun verifyEmail() {
        val user = FirebaseAuth.getInstance().currentUser
        val builder =
            androidx.appcompat.app.AlertDialog.Builder(SignUpActivity.contextSignUpActivity)
        builder.setTitle("Account created")
        builder.setMessage("Confirm email to continue")
        builder.setCancelable(true)

        builder.setPositiveButton(
            "Confirm"
        ) { dialog, _ ->
            user?.sendEmailVerification()?.addOnCompleteListener {
                model.listener!!.toast("Email Sent")
                FirebaseAuth.getInstance().signOut()
                dialog.dismiss()
                model.listener!!.goToLogIn()
            }
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.dismiss() }

        val alert = builder.create()
        alert.show()

    }

}