package dev.ronnie.chama.login

import android.view.View
import androidx.lifecycle.ViewModel

class LogInViewModel : ViewModel() {

    var email: String? = null
    var password: String? = null
    var listener: LogInListener? = null

    fun logIn(view: View) {

        if (email.isNullOrEmpty()) {

            listener!!.viewsEmpty("Email Required", 1)
            return

        }
        if (password.isNullOrEmpty()) {

            listener!!.viewsEmpty("Password Required", 2)
            return

        }

        val logIn = LogIn(this)
        logIn.startLogIn(email!!, password!!)
        listener!!.closeKeyboard()

    }

    fun goToSignUp(view:View) {

        listener!!.goToSignUp()

    }

}