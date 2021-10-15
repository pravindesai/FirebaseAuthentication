package com.pravin.barcodeapp.firebaseauthenticationcompleteusermanagement.Util

import android.app.Application
import android.content.Context

class AuthApplication: Application() {
    companion object {
        lateinit  var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = applicationContext
    }
}