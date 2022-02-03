package com.malec.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import javax.inject.Inject

abstract class BaseActivity<Binding : ViewBinding> : AppCompatActivity(),
    ActivityResultCallback<Any> {
    @Inject
    lateinit var navHolder: NavigatorHolder

    protected abstract val navigator: AppNavigator

    protected lateinit var binding: Binding

    abstract fun createViewBinding(inflater: LayoutInflater): Binding

    protected fun showError(message: String) {
//        val root = findViewById<View>(R.id.root)
//        Snackbar.make(root, message, Snackbar.LENGTH_LONG)
//            .setBackgroundTint(ContextCompat.getColor(this, R.color.colorError))
//            .setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
//            .show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = createViewBinding(layoutInflater)
        setContentView(binding.root)
    }

    protected fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    protected fun showMessage(stringId: Int) {
        showMessage(getString(stringId))
    }

    override fun onPause() {
        super.onPause()
        navHolder.removeNavigator()
    }

    override fun onResume() {
        super.onResume()
        navHolder.setNavigator(navigator)
    }

    override fun onActivityResult(result: Any) {
        supportFragmentManager.fragments.forEach {
            if (it is BaseFragment<*>)
                it.onResult(result)
        }
    }
}