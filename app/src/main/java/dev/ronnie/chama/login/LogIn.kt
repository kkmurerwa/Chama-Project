package dev.ronnie.chama.login

import com.google.firebase.auth.FirebaseAuth

class LogIn(var model: LogInViewModel) {

    fun startLogIn(email: String, password: String) {

        model.listener!!.progressBarVisible()

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
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
        builder.setTitle("Verify Email")
        builder.setMessage("This email address is not verified.")
        builder.setCancelable(true)

        builder.setPositiveButton(
            "Confirm"
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


}