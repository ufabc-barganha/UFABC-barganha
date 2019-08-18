package br.edu.ufabc.ufabcbarganha

import android.Manifest
import android.annotation.SuppressLint
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
import android.util.Log
import br.edu.ufabc.ufabcbarganha.data.firestore.FirestoreDatabaseOperationListener
import br.edu.ufabc.ufabcbarganha.data.firestore.PostDAO
import br.edu.ufabc.ufabcbarganha.model.Post
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.core.Path
import java.io.ByteArrayOutputStream
import java.util.*

class CreatePostActivity : AppCompatActivity() {

    private companion object Constants {
        const val CAMERA_REQUEST_PERMISSIONS = 100
        const val IMAGE_CAPTURE_INTENT = 200
    }

    private lateinit var productTitle: EditText
    private lateinit var productPrice: EditText
    private lateinit var localization: EditText
    private lateinit var productDescription: MultiAutoCompleteTextView
    private lateinit var addPhoto: FloatingActionButton
    private lateinit var createPost: Button
    private lateinit var productPhoto: ImageView
    private lateinit var postType: Spinner

    private lateinit var photoUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        setSupportActionBar(toolbar)

        initializeViews()
        setListeners()

        if(intent.hasExtra(App.POST_TYPE_EXTRA))
            postType.setSelection((postType.adapter as ArrayAdapter<Post.PostType>)
                .getPosition(intent.extras?.get(App.POST_TYPE_EXTRA) as Post.PostType))

    }

    private fun initializeViews() {
        productTitle = findViewById(R.id.create_post_title)
        productPrice = findViewById(R.id.create_post_price)
        localization = findViewById(R.id.create_post_place)
        productDescription = findViewById(R.id.create_post_description)
        addPhoto = findViewById(R.id.add_photo_button)
        createPost = findViewById(R.id.create_post_button)
        productPhoto = findViewById(R.id.product_photo_imageview)
        postType = findViewById(R.id.post_type)

        postType.adapter = ArrayAdapter(
            this,
            R.layout.spinner_layout,
            Post.PostType.values())
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
            PostDAO.add(createPost(), object : FirestoreDatabaseOperationListener<Void?> {
                override fun onSuccess(result: Void?) {
                    Toast.makeText(this@CreatePostActivity, R.string.create_post_success, Toast.LENGTH_LONG).show()
                }

                override fun onFailure(e: Exception) {
                    Toast.makeText(this@CreatePostActivity, R.string.create_post_failure, Toast.LENGTH_LONG).show()
                }

            })
        }
    }

    private fun createPost(): Post{
        val post = Post()

        post.username = "Joao"
        post.productName = productTitle.text.toString()
        post.photo = photoUri.toString()
        post.price = productPrice.text.toString().toDouble()
        post.description = productDescription.toString()
        post.postTime = Calendar.getInstance().time
        post.postType = postType.selectedItem as Post.PostType

        return post
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
                    Snackbar.make(productTitle, R.string.camera_permission_denied_message, Snackbar.LENGTH_LONG).show()
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
            Snackbar.make(productTitle, R.string.no_camera_app, Snackbar.LENGTH_LONG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_CAPTURE_INTENT && resultCode == RESULT_OK) {
            if(data?.hasExtra("data") as Boolean) {
                val photo = data.extras?.get("data") as Bitmap

                productPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
                productPhoto.setImageBitmap(photo)

                photoUri = getImageUri(photo)
                Log.e("igor", photoUri.toString())

            } else {
                photoUri =  data.data!!
                productPhoto.setImageURI(photoUri)

                Log.e("igor", photoUri.toString())

                /*if (photoURI == null)
                    Toast.makeText(
                        this,
                        getString(R.string.no_image),
                        Toast.LENGTH_LONG).show()*/
            }
        }
    }

    fun getImageUri(image: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        val path = MediaStore.Images.Media.insertImage(this.contentResolver, image, "Title", null)

        return Uri.parse(path)
    }


    @SuppressLint("RestrictedApi")
    fun uploadPhotoUri(): Path {
        // Create a storage reference from our app
        val storageRef = FirebaseDatabase.getInstance().reference
        // Create a reference to "mountains.jpg"
        val photoRef = storageRef.child(photoUri.toString())

        return photoRef.path
    }


}
