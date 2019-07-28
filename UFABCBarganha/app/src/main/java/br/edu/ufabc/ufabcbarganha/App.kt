package br.edu.ufabc.ufabcbarganha

import android.app.Application
import android.content.Context

class App: Application() {

    companion object {
        lateinit var context: Context
            private set

        const val PRODUCT_POSITION = "productPosition"
        const val HOUSING_POSITION = "housingPosition"
        const val FOOD_POSITION = "foodPosition"
    }

    override fun onCreate() {
        super.onCreate()

        context = applicationContext
    }
}