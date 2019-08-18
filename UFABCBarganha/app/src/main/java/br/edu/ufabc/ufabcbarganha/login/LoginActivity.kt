package br.edu.ufabc.ufabcbarganha.login

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import br.edu.ufabc.ufabcbarganha.App
import br.edu.ufabc.ufabcbarganha.feed.general.FeedActivity
import br.edu.ufabc.ufabcbarganha.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class LoginActivity : AppCompatActivity() {

    lateinit var rellay1: RelativeLayout
    lateinit var rellay2: RelativeLayout
    lateinit var bgVideo: VideoView
    lateinit var loginButton: Button
    lateinit var forgotPassButton: Button
    lateinit var registerButton: Button
    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText

    private val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

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

        if(intent.getBooleanExtra(App.IS_RETURN, false)){
            delay = 0
        }

        handler.postDelayed(runnable, delay)

    }

    override fun onResume() {
        super.onResume()
        bgVideo.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        bgVideo.suspend()
    }

    override fun onStart() {
        super.onStart()
        goToFeed(mAuth.currentUser)
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

    private fun runVideo(){
        bgVideo.setVideoURI(Uri.parse("android.resource://"+packageName+"/"+R.raw.login_video))
        bgVideo.start()
        bgVideo.setOnPreparedListener{
                mediaPlayer: MediaPlayer -> mediaPlayer.isLooping = true
        }
    }

    private fun setListeners() {
        loginButton.setOnClickListener{
            login()
        }

        registerButton.setOnClickListener{
            register()
        }

        forgotPassButton.setOnClickListener{
            forgotPassword()
        }
    }

    private fun login() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (!email.isEmpty() && !password.isEmpty()) {
            mAuth.signInWithEmailAndPassword(email, password).
                addOnCompleteListener(this, OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        goToFeed(mAuth.currentUser)
                    } else {
                        Toast.makeText(this, R.string.wrong_credentials, Toast.LENGTH_LONG).show()
                    }
                })

        } else {
            Toast.makeText(this, R.string.fill_credentials, Toast.LENGTH_LONG).show()
        }
    }

    private fun register() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

    private fun forgotPassword() {
        startActivity(Intent(this, ForgotPassActivity::class.java))
    }

    private fun goToFeed(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            if (currentUser.isEmailVerified) {
                //startActivity(Intent(this, FeedActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, R.string.verify_email, Toast.LENGTH_LONG).show()
            }
        }
    }

}
