package com.example.autocars.ui.base

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat

import com.example.autocars.MainApplication
import com.example.autocars.R
import com.google.android.material.snackbar.Snackbar
import es.dmoral.toasty.Toasty
import io.github.inflationx.viewpump.ViewPumpContextWrapper

//import io.github.inflationx.viewpump.ViewPumpContextWrapper


/**
 * Created by root on 9/24/17.
 */
abstract class BaseActivity : AppCompatActivity() {
    var snackbar: Snackbar? = null

    @Volatile
    private var isOn = false
    var view1: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val application: MainApplication = application as MainApplication
    }

    fun setToolbar(toolbar: Toolbar?, title: String?) {
        setSupportActionBar(toolbar)
        if (supportActionBar != null) {
            supportActionBar!!.title = title
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        super.setContentView(layoutResID)
        view1 = getView()
        if (view1 != null) {
            snackbar = Snackbar.make(view1!!, "Check your internet connection.", Snackbar.LENGTH_INDEFINITE)
            val snackBarView = snackbar!!.view
            snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.red))
            val textView = snackBarView.findViewById<View>(R.id.snackbar_text) as TextView
            textView.gravity = View.TEXT_ALIGNMENT_CENTER
            textView.setTextColor(ContextCompat.getColor(this, R.color.white))
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    protected fun snackBar(message: String?) {
        if (view1 != null) {
            Snackbar.make(view1!!, message!!, Snackbar.LENGTH_LONG).show()
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
    }

    override fun onPause() {
        unregisterReceiver(broadcastReceiver)
        Util.hideKeyboard(this)
        super.onPause()
        isOn = false
    }

    override fun onResume() {
        super.onResume()
        registerInternetCheckReceiver()
        isOn = true
    }

    private fun registerInternetCheckReceiver() {
        val internetFilter = IntentFilter()
        internetFilter.addAction("android.net.wifi.STATE_CHANGE")
        internetFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE")
        registerReceiver(broadcastReceiver, internetFilter)
    }

    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (Util.isOnline) {
                if (snackbar != null) snackbar!!.dismiss()
            } else {
                if (snackbar != null) snackbar!!.show()
            }
        }
    }

    protected fun toastSuccess(msg: String?) {
        Toasty.success(MainApplication.instance!!, msg!!, Toast.LENGTH_SHORT, true).show()
    }

    protected fun toastError(msg: String?) {
        Toasty.error(MainApplication.instance!!, msg!!, Toast.LENGTH_SHORT, true).show()
    }

    protected fun toastInfo(msg: String?) {
        Toasty.info(MainApplication.instance!!, msg!!, Toast.LENGTH_SHORT, true).show()
    }

    protected fun toastWarning(msg: String?) {
        Toasty.warning(MainApplication.instance!!, msg!!, Toast.LENGTH_SHORT, true).show()
    }

    private fun getView(): View? {
        val vg = findViewById<ViewGroup>(android.R.id.content)
        var rv: View? = null
        if (vg != null) rv = vg.getChildAt(0)
        if (rv == null) rv = window.decorView.rootView
        return rv
    }
}