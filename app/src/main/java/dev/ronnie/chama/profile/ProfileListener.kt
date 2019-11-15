package dev.ronnie.chama.profile

import androidx.lifecycle.LiveData
import dev.ronnie.chama.models.User

interface ProfileListener {

    fun displayProfile(user: LiveData<User>)
    fun showDialog()
    fun progressBarVisible()
    fun progressBarGone()
    fun toast(message: String)
    fun enableSaveButton()
    fun disableSaveButton()
    fun openPicture()

}