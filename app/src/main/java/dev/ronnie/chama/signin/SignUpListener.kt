package dev.ronnie.chama.signin

interface SignUpListener {

    fun viewsEmpty(message: String, code: Int)
    fun error(message: String, code: Int)
    fun progressBarVisible()
    fun progressBarGone()
    fun toast(message: String)
    fun goToLogIn()
    fun goToProfile()
    fun closeKeyboard()
}