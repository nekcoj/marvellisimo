package com.example.marvellisimo.firebase

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.marvellisimo.MainActivity
import com.example.marvellisimo.data.Service.Companion._menu
import com.example.marvellisimo.data.Service.Companion.toggleNavbarItemsIfAuth
import com.google.firebase.auth.FirebaseAuth


class FirebaseFunctions {
    companion object {
        lateinit var user: UserModel

        /*fun makeUserOnline() {
            var query = FirebaseFirestore.getInstance().collection("users").document(user.userId ?: "")
            user.apply {
                online = true
                last_active = 0
            }
            query.set(user)
        }*/

        fun logoutUser() {
            FirebaseAuth.getInstance().signOut()
            toggleNavbarItemsIfAuth(_menu)
        }
    }
}

class UserModel(
    var userId: String? = null,
    var online: Boolean = false,
    var last_active: Int
)