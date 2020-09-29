package com.example.marvellisimo.firebase

import com.example.marvellisimo.data.Service.Companion.menu_
import com.example.marvellisimo.data.Service.Companion.toggleNavbarItemsIfAuth
import com.google.firebase.auth.FirebaseAuth


class FirebaseFunctions {
    companion object {
        fun logoutUser() {
            FirebaseAuth.getInstance().signOut()
            toggleNavbarItemsIfAuth(menu_)
        }
    }
}