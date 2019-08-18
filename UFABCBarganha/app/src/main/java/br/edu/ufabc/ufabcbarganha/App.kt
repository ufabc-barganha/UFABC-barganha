package br.edu.ufabc.ufabcbarganha

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.localbroadcastmanager.content.LocalBroadcastManager

class App: Application() {

    companion object {
        lateinit var appContext: Context
            private set

        const val POST_EXTRA = "extra_post"
        const val IS_RETURN = "isReturn"
        const val POST_TYPE_EXTRA = "post_type_extra"

        const val PHOTO_UPLOADED = "photoUploaded"
        const val PHOTO_PATH_EXTRA = "photoPathExtra"

        fun registerBroadcast(receiver: BroadcastReceiver, filter: IntentFilter) {
            LocalBroadcastManager.getInstance(appContext).registerReceiver(receiver, filter)
        }

        fun unregisterBroadcast(receiver: BroadcastReceiver) {
            LocalBroadcastManager.getInstance(appContext).unregisterReceiver(receiver)
        }

        fun sendBroadcast(intent: Intent) {
            LocalBroadcastManager.getInstance(appContext).sendBroadcast(intent)
        }

    }

    override fun onCreate() {
        super.onCreate()

        appContext = applicationContext
    }
}