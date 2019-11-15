package dev.ronnie.chama.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dev.ronnie.chama.models.User

class Profile(model: ProfileViewModel) {

    companion object {
        var profile: String? = null
    }

    fun getProfile(): LiveData<User> {

        val reference = FirebaseDatabase.getInstance().reference
        val user = MutableLiveData<User>()

        val query1 = reference.child("Users")
            .orderByKey()
            .equalTo(FirebaseAuth.getInstance().currentUser!!.uid)
        query1.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (singleSnapshot in dataSnapshot.children) {

                    user.value = singleSnapshot.getValue(User::class.java)
                    val userProfile = singleSnapshot.getValue(User::class.java)
                    profile = userProfile!!.profile_image

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        return user
    }

}