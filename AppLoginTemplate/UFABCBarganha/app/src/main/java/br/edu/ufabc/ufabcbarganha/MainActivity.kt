package br.edu.ufabc.ufabcbarganha

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.RelativeLayout

class MainActivity : AppCompatActivity() {

    private lateinit var relativeLayoutForms: RelativeLayout
    private lateinit var relativeLayoutBottom: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindComponents()
    }

    private fun bindComponents() {
        this.relativeLayoutForms = findViewById(R.id.relative_layout_forms)
        this.relativeLayoutBottom = findViewById(R.id.relative_layout_bottom)

        val handler = Handler()
        val runnable = Runnable {
            this.relativeLayoutForms.visibility = View.VISIBLE
            this.relativeLayoutBottom.visibility = View.VISIBLE
        }

        handler.postDelayed(runnable, 2000)
    }
}
