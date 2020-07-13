package com.malec.cheesetime.repo

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.DocumentReference

fun authCompleteListener(
    onSuccess: () -> Unit,
    onFailure: (t: Throwable?) -> Unit
) = OnCompleteListener<AuthResult> {
    if (it.isSuccessful)
        onSuccess()
    else
        onFailure(it.exception)
}

fun dataCompleteListener(
    onSuccess: () -> Unit,
    onFailure: (t: Throwable?) -> Unit
) = OnCompleteListener<DocumentReference> {
    if (it.isSuccessful)
        onSuccess()
    else
        onFailure(it.exception)
}