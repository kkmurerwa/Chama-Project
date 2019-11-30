package dev.ronnie.chama.login

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.iid.FirebaseInstanceId

class LogIn(var model: LogInViewModel) {

    fun startLogIn(email: String, password: String) {

        model.listener!!.progressBarVisible()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    initFCM()
                    model.listener!!.progressBarGone()

                    if (FirebaseAuth.getInstance().currentUser!!.isEmailVerified) {
                        model.listener!!.goToLandingPage()
                    } else {
                        verifyEmail()
                    }

                }
            }.addOnFailureListener {
                model.listener!!.toast(it.message.toString())
                model.listener!!.progressBarGone()
            }

    }

    private fun verifyEmail() {
        val user = FirebaseAuth.getInstance().currentUser
        val builder = androidx.appcompat.app.AlertDialog.Builder(LogInActivity.contextLogInActivity)
        builder.setMessage("This email address is not verified.")
        builder.setCancelable(true)

        builder.setPositiveButton(
            "Verify"
        ) { dialog, _ ->
            user?.sendEmailVerification()?.addOnCompleteListener {
                model.listener!!.toast("Email Sent")

                dialog.dismiss()

            }
        }
        builder.setNegativeButton(
            "Cancel"
        ) { dialog, _ -> dialog.dismiss() }

        val alert = builder.create()
        alert.show()


    }

    fun initFCM() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnSuccessListener { instanceIdResult ->
                val deviceToken = instanceIdResult.token

                Log.d("Token", "New Token $deviceToken")
                sendRegistrationToServer(deviceToken)
            }
    }

    private fun sendRegistrationToServer(token: String?) {
        if (FirebaseAuth.getInstance().currentUser != null) {
            Log.d(
                "MessagingService",
                "sendRegistrationToServer: sending token to server: $token"
            )
            val reference = FirebaseDatabase.getInstance().reference
            reference.child("Users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("messaging_token")
                .setValue(token)
        }
    }
}