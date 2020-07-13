package com.malec.cheesetime.repo

import com.google.firebase.auth.FirebaseAuth

class UserRepo {
    private val auth = FirebaseAuth.getInstance()

    fun isUserAuthorized() = auth.currentUser != null

    fun register(
        email: String,
        pass: String,
        onSuccess: () -> Unit,
        onFailure: (t: Throwable?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(authCompleteListener(onSuccess, onFailure))
    }

    fun login(
        email: String,
        pass: String,
        onSuccess: () -> Unit,
        onFailure: (t: Throwable?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(authCompleteListener(onSuccess, onFailure))
    }
}