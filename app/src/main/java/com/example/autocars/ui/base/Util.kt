package com.example.autocars.ui.base


import android.app.Activity
import android.content.Context

import android.net.ConnectivityManager
import android.os.Environment

import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import coil.ImageLoader
import coil.api.load
import coil.decode.SvgDecoder
import coil.request.LoadRequest


import com.example.autocars.MainApplication
import com.example.autocars.R
import com.github.chrisbanes.photoview.PhotoView
import java.io.File

import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


/**
 * 6/13/2017.
 */

object Util {
    private val SIMPLE_DATE_FORMAT = SimpleDateFormat("dd_mm_yyyy_hh_mm_ss", Locale.ENGLISH)

    val isOnline: Boolean
        get() {
            if (MainApplication.instance!!.applicationContext == null)
                return false

            val cm = MainApplication.instance!!.applicationContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val activeNetwork = cm.activeNetworkInfo
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting
        }

    private val naira: String
        get() = MainApplication.instance!!.getString(R.string.naira)

    val outputMediaFile: File?
        get() {
            val mediaStorageDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "EagleEye"
            )

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null
                }
            }

            val timeStamp = SIMPLE_DATE_FORMAT.format(Date())
            return File(mediaStorageDir.path + File.separator + "IMG_" + timeStamp + ".jpg")
        }

    fun getNairaUnitFormat(amount: String): String {
        try {
            val amounts = amount + "D"
            val format = NumberFormat.getCurrencyInstance(Locale.CANADA)
            val currency = format.format(java.lang.Double.parseDouble(amounts))
            return naira + currency.replace("$", "")
        }catch (e : Exception){
            return ""
        }
    }




    fun getNairaUnitFormat(amount: String, addNaira: Boolean): String? {
        val nairaFormat = getNairaUnitFormat(amount)
        return if (addNaira)  nairaFormat else nairaFormat
    }


    fun showKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = activity.currentFocus
        if (view == null) {
            view = View(activity)
        }
        assert(imm != null)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun networkMessage(): String{
        return "Sorry kindly check your internet connection!!"
    }


    private fun PhotoView.loadSvgOrOthers(myUrl: String?) {
        myUrl?.let {
            if (it.toLowerCase(Locale.ENGLISH).endsWith("svg")) {
                val imageLoader = ImageLoader.Builder(this.context)
                        .componentRegistry {
                            add(SvgDecoder(this@loadSvgOrOthers.context))
                        }
                        .build()
                val request = LoadRequest.Builder(this.context)
                        .data(it)
                        .target(this)
                        .placeholder(R.drawable.carlogoicon)
                        .build()
                imageLoader.execute(request)
            } else {
                this.load(myUrl)
            }
        }
    }


}

