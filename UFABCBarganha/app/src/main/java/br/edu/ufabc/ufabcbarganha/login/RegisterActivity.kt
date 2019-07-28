package br.edu.ufabc.ufabcbarganha.login

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import br.edu.ufabc.ufabcbarganha.R

class RegisterActivity : AppCompatActivity() {

    lateinit var bgVideo: VideoView
    lateinit var registerButton: Button
    lateinit var emailEditText: EditText
    lateinit var confirmEmailEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var confirmPasswordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setViews()
        setListeners()
        runVideo()
    }

    private fun setListeners() {
        registerButton.setOnClickListener{
            startActivity(Intent(this, LoginActivity::class.java))
            Toast.makeText(this, R.string.check_your_email, Toast.LENGTH_LONG)
        }
    }

    private fun runVideo(){
        bgVideo.setVideoURI(Uri.parse("android.resource://"+packageName+"/"+R.raw.login_video))
        bgVideo.start()
        bgVideo.setOnPreparedListener{
                mediaPlayer: MediaPlayer -> mediaPlayer.isLooping = true
        }
    }

    private fun setViews() {
        bgVideo = findViewById(R.id.loginVideoView)
        registerButton = findViewById(R.id.registerButton)

        emailEditText = findViewById(R.id.emailEditText)
        confirmEmailEditText = findViewById(R.id.confirmEmailEditText)

        passwordEditText = findViewById(R.id.passwordEditText)
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText)
    }

}


