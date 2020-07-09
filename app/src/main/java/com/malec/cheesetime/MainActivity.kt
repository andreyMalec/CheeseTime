package com.malec.cheesetime

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.malec.cheesetime.ui.CheeseManageActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val i = Intent(this, CheeseManageActivity::class.java)
        startActivity(i)
        finish()
    }
}