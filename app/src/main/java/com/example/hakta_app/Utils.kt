package com.example.hakta_app

import android.app.AlertDialog
import android.content.Context

fun Context.errorAlert(message: String) {
    val res = this.resources
    AlertDialog.Builder(this)
        .setTitle(res.getString(R.string.error_title))
        .setMessage(message)
        .setPositiveButton(res.getString(R.string.ok), null)
        .create()
        .show()
}