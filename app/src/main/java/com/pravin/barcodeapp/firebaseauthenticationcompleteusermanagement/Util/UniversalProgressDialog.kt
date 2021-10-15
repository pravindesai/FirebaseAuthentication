package com.pravin.barcodeapp.firebaseauthenticationcompleteusermanagement.Util

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import com.pravin.barcodeapp.firebaseauthenticationcompleteusermanagement.R

object UniversalProgressDialog {
    lateinit var progressDialog:AlertDialog
    lateinit var alertDialogBuilder:AlertDialog.Builder;

    fun show(context: Context) {
        alertDialogBuilder = AlertDialog.Builder(context, R.style.wrapdialogstyle)
        alertDialogBuilder.setCancelable(false)
        alertDialogBuilder.setView(R.layout.dialogview)
        progressDialog = alertDialogBuilder.create()
        progressDialog.getWindow()?.setBackgroundDrawableResource(android.R.color.transparent);

        if (progressDialog.isShowing) return
        progressDialog.show()
    }


    fun hide(){
        if (!progressDialog.isShowing) return
        progressDialog.hide()
    }
}