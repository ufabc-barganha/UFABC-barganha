package br.edu.ufabc.ufabcbarganha.login

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.*
import br.edu.ufabc.ufabcbarganha.App
import br.edu.ufabc.ufabcbarganha.feed.general.FeedActivity
import br.edu.ufabc.ufabcbarganha.R

class LoginActivity : AppCompatActivity() {

    lateinit var rellay1: RelativeLayout
    lateinit var rellay2: RelativeLayout
    lateinit var bgVideo: VideoView
    lateinit var loginButton: Button
    lateinit var forgotPassButton: Button
    lateinit var registerButton: Button
    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText

    var handler = Handler()
    var runnable = Runnable {
        rellay1.visibility = View.VISIBLE
        rellay2.visibility = View.VISIBLE
        runVideo()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setViews()
        setListeners()
        var delay = 2000L
        val ret = intent.getBooleanExtra(App.IS_RETURN, false)
        if(ret){
            delay = 0
        }
        //exibe o logo por 2000 ms e mostra o login com o vídeo no fundo
        handler.postDelayed(runnable, delay)

    }

    private fun setViews() {
        rellay1 = findViewById(R.id.rellay1)
        rellay2 = findViewById(R.id.rellay2)
        bgVideo = findViewById(R.id.loginVideoView)
        forgotPassButton = findViewById(R.id.forgotPassButton)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
    }

    private fun setListeners() {
        loginButton.setOnClickListener{
            startActivity(Intent(this, FeedActivity::class.java))
        }

        registerButton.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        forgotPassButton.setOnClickListener{
            startActivity(Intent(this, ForgotPassActivity::class.java))
        }
    }

    private fun runVideo(){
        bgVideo.setVideoURI(Uri.parse("android.resource://"+packageName+"/"+R.raw.login_video))
        bgVideo.start()
        bgVideo.setOnPreparedListener{
                mediaPlayer: MediaPlayer -> mediaPlayer.isLooping = true
        }
    }

    override fun onResume() {
        super.onResume()
        bgVideo.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        bgVideo.suspend()
    }
}
