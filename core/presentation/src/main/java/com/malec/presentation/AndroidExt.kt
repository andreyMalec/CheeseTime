package com.malec.presentation

import android.view.View
import android.view.WindowInsets
import android.widget.ArrayAdapter
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.updatePadding

fun <T> ArrayAdapter<T>.update(data: List<T>) {
    clear()
    addAll(data)
    notifyDataSetChanged()
}

fun ActionBar?.isDisplayHomeAsUpEnabled(): Boolean {
    val options = this?.displayOptions ?: 0
    return (options and ActionBar.DISPLAY_HOME_AS_UP) != 0
}

fun AppCompatActivity.initToolbar(root: View, toolBar: Toolbar, titleResId: Int) {
    initToolbar(root, toolBar, getString(titleResId))
}

fun AppCompatActivity.initToolbar(root: View, toolBar: Toolbar, title: String) {
    toolBar.title = title
    setSupportActionBar(toolBar)
    root.setOnApplyWindowInsetsListener { _, insets ->
        val statusBarHeight =
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R)
                insets.getInsets(WindowInsets.Type.systemBars()).top
            else
                insets.systemWindowInsetTop

        val h = (resources.getDimension(R.dimen.toolbar_height) + statusBarHeight).toInt()
        val lp = toolBar.layoutParams
        toolBar.layoutParams = lp.also { it.height = h }
        toolBar.updatePadding(top = statusBarHeight)
        insets
    }
}