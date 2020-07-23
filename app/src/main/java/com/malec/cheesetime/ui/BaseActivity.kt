package com.malec.cheesetime.ui

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.malec.cheesetime.R
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

open class BaseActivity : AppCompatActivity(), HasAndroidInjector {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector

    protected fun showError(message: String) {
        val root = findViewById<View>(R.id.root)
        Snackbar.make(root, message, Snackbar.LENGTH_LONG)
            .setBackgroundTint(ContextCompat.getColor(this, R.color.colorError))
            .setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
            .show()
    }

    protected fun showMessage(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        toast.view.background.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(this, R.color.backgroundLight),
            PorterDuff.Mode.SRC_IN
        )
        toast.view.findViewById<TextView>(android.R.id.message).setTextColor(
            ContextCompat.getColor(this, R.color.colorAccent)
        )
        toast.show()
    }
}