package com.example.mytaskmanager.utils

import android.content.Intent
import androidx.core.app.ActivityOptionsCompat
import com.example.mytaskmanager.MainActivity
import com.example.mytaskmanager.R

inline fun android.content.Context.launchNextPage(
    fragmentType: String,
    init: Intent.() -> Unit = {},
    finishAll: Boolean = false,
    finish: Boolean = false
) {
    val options = ActivityOptionsCompat.makeCustomAnimation(
        this,
        R.anim.enter,
        R.anim.exit
    )
    val intent = Intent(this, MainActivity::class.java)
    intent.putExtra(getString(R.string.name), fragmentType)
    intent.init()
    startActivity(intent, options.toBundle())
    if (finishAll) {
        (this as android.app.Activity).finishAffinity()
    } else if (finish) {
        (this as android.app.Activity).finish()
    }
}
