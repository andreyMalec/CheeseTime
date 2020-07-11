package com.malec.cheesetime.repo

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class UserRepo {
    private val auth = FirebaseAuth.getInstance()

    fun isUserAuthorized() = auth.currentUser != null

    private fun completeListener(
        onSuccess: () -> Unit,
        onFailure: (t: Throwable?) -> Unit
    ) = OnCompleteListener<AuthResult> {
        if (it.isSuccessful)
            onSuccess()
        else
            onFailure(it.exception)
    }

    fun register(
        email: String,
        pass: String,
        onSuccess: () -> Unit,
        onFailure: (t: Throwable?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener(completeListener(onSuccess, onFailure))
    }

    fun login(
        email: String,
        pass: String,
        onSuccess: () -> Unit,
        onFailure: (t: Throwable?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener(completeListener(onSuccess, onFailure))
    }
}