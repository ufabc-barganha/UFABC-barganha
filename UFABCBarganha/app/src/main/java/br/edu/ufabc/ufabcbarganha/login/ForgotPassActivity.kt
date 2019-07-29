package br.edu.ufabc.ufabcbarganha.login

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.VideoView
import br.edu.ufabc.ufabcbarganha.App
import br.edu.ufabc.ufabcbarganha.R

class ForgotPassActivity : AppCompatActivity() {

    lateinit var bgVideo: VideoView
    lateinit var sendEmailButton: Button
    lateinit var emailEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_pass)

        setViews()
        setListeners()
        runVideo()
    }

    private fun runVideo(){
        bgVideo.setVideoURI(Uri.parse("android.resource://"+packageName+"/"+R.raw.login_video))
        bgVideo.start()
        bgVideo.setOnPreparedListener{
                mediaPlayer: MediaPlayer -> mediaPlayer.isLooping = true
        }
    }

    private fun setListeners() {
        sendEmailButton.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra(App.IS_RETURN, true)
            Toast.makeText(this, R.string.check_your_email, Toast.LENGTH_LONG).show()
            startActivity(intent)
        }
    }

    private fun setViews() {
        bgVideo = findViewById(R.id.loginVideoView)
        emailEditText = findViewById(R.id.emailEditText)
        sendEmailButton = findViewById(R.id.sendEmailButton)
    }

    override fun onResume() {
        super.onResume()
        bgVideo.start()
    }
}
