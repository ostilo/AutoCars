package com.example.autocars

import android.content.Context
import android.net.ConnectivityManager
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        instance = this

    }
    companion object {
        val executorService =
            Executors.newCachedThreadPool()
        var instance: MainApplication? = null
            private set

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
    fun getExecutorService(): ExecutorService? {
        return executorService
    }

    fun getInstance(): MainApplication? {
        return instance
    }

    fun isConnectionActive(contexts: Context): Boolean {
        val connectivityManager = contexts.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }

}
