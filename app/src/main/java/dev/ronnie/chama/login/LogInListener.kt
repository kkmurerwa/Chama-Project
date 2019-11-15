package dev.ronnie.chama.login

interface LogInListener {

    fun goToSignUp()
    fun viewsEmpty(message: String, code: Int)
    fun progressBarVisible()
    fun progressBarGone()
    fun goToLandingPage()
    fun toast(message: String)
    fun closeKeyboard()
}