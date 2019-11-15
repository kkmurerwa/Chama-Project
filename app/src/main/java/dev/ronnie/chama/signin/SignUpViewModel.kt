package dev.ronnie.chama.signin

import android.view.View
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {

    var email: String? = null
    var password: String? = null
    var confirmPassword: String? = null
    var listener: SignUpListener? = null

    fun register(view: View) {

        if (email.isNullOrEmpty()) {

            listener!!.viewsEmpty("Email Required", 1)
            return

        }
        if (password.isNullOrEmpty()) {

            listener!!.viewsEmpty("Password Required", 2)
            return

        }
        if (confirmPassword.isNullOrEmpty()) {

            listener!!.viewsEmpty("Password confirmation Required", 3)
            return

        }
        if (!password.equals(confirmPassword)) {
            listener!!.error("Passwords did not match", 1)
            return

        }
        if (password!!.length < 6) {
            listener!!.error("Passwords should be at least six characters", 2)
            return
        }

        val register = Register(this)
        register.startRegistering(email!!, password!!)
        listener!!.progressBarVisible()
        listener!!.closeKeyboard()

    }

    fun goToLogIn(view: View) {
        listener!!.goToLogIn()

    }


}