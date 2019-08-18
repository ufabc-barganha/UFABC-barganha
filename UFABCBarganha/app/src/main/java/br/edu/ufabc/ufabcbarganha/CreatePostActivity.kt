package br.edu.ufabc.ufabcbarganha

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

import kotlinx.android.synthetic.main.activity_create_post.*
import android.graphics.Bitmap
import android.net.Uri


class CreatePostActivity : AppCompatActivity() {

    private companion object Constants {
        const val CAMERA_REQUEST_PERMISSIONS = 100
        const val IMAGE_CAPTURE_INTENT = 200
    }


    lateinit var title: EditText
    lateinit var price: EditText
    lateinit var localization: EditText
    lateinit var description: MultiAutoCompleteTextView
    lateinit var addPhoto: FloatingActionButton
    lateinit var createPost: Button
    lateinit var productPhoto: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        setSupportActionBar(toolbar)

        initializeViews()
        setListeners()

    }

    private fun initializeViews() {
        title = findViewById(R.id.create_post_title)
        price = findViewById(R.id.create_post_price)
        localization = findViewById(R.id.create_post_place)
        description = findViewById(R.id.create_post_description)
        addPhoto = findViewById(R.id.add_photo_button)
        createPost = findViewById(R.id.create_post_button)
        productPhoto = findViewById(R.id.product_photo_imageview)
    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            finish()
        }

        addPhoto.setOnClickListener{
            val cameraPermission = checkPermissions(Manifest.permission.CAMERA)
            if(cameraPermission)callCamera()
            else ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_PERMISSIONS)
        }

        createPost.setOnClickListener{

        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun checkPermissions(permission: String): Boolean {
        return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_PERMISSIONS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    callCamera()
                } else {
                    Snackbar.make(title, R.string.camera_permission_denied_message, Snackbar.LENGTH_LONG).show()
                }
                return
            }

            else -> {
                // Ignore all other requests.
            }

        }
    }

    fun callCamera(){
        val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val choosePicture = Intent(Intent.ACTION_GET_CONTENT)
        choosePicture.type = "image/*"
        val chooser = Intent.createChooser(choosePicture, getString(R.string.choose_image_app))
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(takePicture))

        if (intent.resolveActivity(packageManager) != null)
            startActivityForResult(chooser, IMAGE_CAPTURE_INTENT)
        else
            Snackbar.make(title, R.string.no_camera_app, Snackbar.LENGTH_LONG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_CAPTURE_INTENT && resultCode == RESULT_OK) {
            if(data?.hasExtra("data") as Boolean) {
                val photo = data.extras?.get("data") as Bitmap
                productPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
                productPhoto.setImageBitmap(photo)
            } else {
                val photoURI : Uri? = data.data
                productPhoto.setImageURI(photoURI)

                /*if (photoURI == null)
                    Toast.makeText(
                        this,
                        getString(R.string.no_image),
                        Toast.LENGTH_LONG).show()*/
            }
        }
    }
}
