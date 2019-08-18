package br.edu.ufabc.ufabcbarganha

import android.app.Application
import android.content.Context

class App: Application() {

    companion object {
        lateinit var context: Context
            private set

        const val POST_EXTRA = "extra_post"
        const val IS_RETURN = "isReturn"
        const val POST_TYPE_EXTRA = "post_type_extra"
    }

    override fun onCreate() {
        super.onCreate()

        context = applicationContext
    }
}