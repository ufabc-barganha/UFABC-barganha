package br.edu.ufabc.ufabcbarganha.login

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import br.edu.ufabc.ufabcbarganha.App
import br.edu.ufabc.ufabcbarganha.R
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import kotlinx.android.synthetic.main.activity_register.*

@Suppress("NAME_SHADOWING")
class RegisterActivity : AppCompatActivity() {

    lateinit var bgVideo: VideoView
    lateinit var registerButton: Button
    lateinit var emailEditText: EditText
    lateinit var confirmEmailEditText: EditText
    lateinit var passwordEditText: EditText
    lateinit var confirmPasswordEditText: EditText

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        setViews()
        setListeners()
        runVideo()
    }

    private fun setListeners() {
        registerButton.setOnClickListener{
            register()
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

    override fun onResume() {
        super.onResume()
        bgVideo.start()
    }

    private fun register() {
        val name = nameEditText.text.toString()
        val email = emailEditText.text.toString()
        val emailConfirmation = confirmEmailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val passwordConfirmation = confirmPasswordEditText.text.toString()

        val regex = "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@((ufabc.edu.br)|(aluno.ufabc.edu.br))".toRegex()

        when {
            name.isEmpty()-> Toast.makeText(this, R.string.empty_name, Toast.LENGTH_LONG).show()

            email != emailConfirmation -> Toast.makeText(this, R.string.different_emails, Toast.LENGTH_LONG).show()

            password != passwordConfirmation -> Toast.makeText(this, R.string.different_passwords, Toast.LENGTH_LONG).show()

            !regex.containsMatchIn(email) -> Toast.makeText(this, R.string.valid_email, Toast.LENGTH_LONG).show()

            else -> createUser(email, password)
        }

    }

    private fun createUser(email: String, password: String) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, OnCompleteListener { task ->
            if (task.isSuccessful) {
                val user: FirebaseUser? = mAuth.currentUser

                user?.sendEmailVerification()?.addOnCompleteListener { task ->
                    if (task.isSuccessful)
                        goToFeed()
                }
            } else
                Toast.makeText(this, R.string.an_error_occured, Toast.LENGTH_LONG).show()
        })
    }

    private fun goToFeed() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

}


