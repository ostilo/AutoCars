package com.example.autocars.ui.activities


import android.os.Bundle
import com.example.autocars.R
import com.example.autocars.ui.base.BaseActivity
import com.example.autocars.ui.base.Util

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Util.hideKeyboard(this)

    }
}