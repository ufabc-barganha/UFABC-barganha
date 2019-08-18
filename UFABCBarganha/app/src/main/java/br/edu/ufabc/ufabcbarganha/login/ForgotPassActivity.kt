package br.edu.ufabc.ufabcbarganha.login

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.VideoView
import br.edu.ufabc.ufabcbarganha.App
import br.edu.ufabc.ufabcbarganha.R
import com.google.firebase.auth.FirebaseAuth

class ForgotPassActivity : AppCompatActivity() {

    lateinit var bgVideo: VideoView
    lateinit var sendEmailButton: Button
    lateinit var emailEditText: EditText

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

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
            forgotPassword()
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

    private fun forgotPassword() {
        val email = emailEditText.text.toString()

        val regex = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@((ufabc.edu.br)|(aluno.ufabc.edu.br))".toRegex()


        when {
            email.isEmpty() -> Toast.makeText(this, R.string.empty_mail, Toast.LENGTH_LONG).show()

            !regex.containsMatchIn(email) -> Toast.makeText(this, R.string.valid_email, Toast.LENGTH_LONG).show()

            else -> changePasswordAndReturn()
        }
    }

    private fun changePasswordAndReturn() {
        val email = emailEditText.text.toString()

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, R.string.check_your_email, Toast.LENGTH_LONG).show()

                Intent(this, LoginActivity::class.java).apply {
                    putExtra(App.IS_RETURN, true)
                    startActivity(this)
                }

                finish()
            }
        }
    }

}
