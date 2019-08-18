package br.edu.ufabc.ufabcbarganha

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
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
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import br.edu.ufabc.ufabcbarganha.data.firestore.FirestoreDatabaseOperationListener
import br.edu.ufabc.ufabcbarganha.data.firestore.PostDAO
import br.edu.ufabc.ufabcbarganha.model.Post
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import br.edu.ufabc.ufabcbarganha.user.data.FirebaseUserHelper
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
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
            if(cameraPermission)
                callCamera()
            else
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_PERMISSIONS)
        }

        createPost.setOnClickListener{
            uploadPhotoAndPost()
        }
    }

    private val photoPathReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.takeIf { it.hasExtra(App.PHOTO_PATH_EXTRA) }?.apply {
                val photoPath = intent.getStringExtra(App.PHOTO_PATH_EXTRA)
                val post = createPost(photoPath)

                PostDAO.add(post, object : FirestoreDatabaseOperationListener<Void?> {
                    override fun onSuccess(result: Void?) {
                        Toast.makeText(this@CreatePostActivity, R.string.create_post_success, Toast.LENGTH_LONG).show()
                    }

                    override fun onFailure(e: Exception) {
                        Toast.makeText(this@CreatePostActivity, R.string.create_post_failure, Toast.LENGTH_LONG).show()
                    }

                })
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun createPost(photoPath: String): Post{
        val post = Post()

        post.username = FirebaseUserHelper.getUserEmail()
        post.productName = productTitle.text.toString()
        post.photo = photoPath
        post.price = productPrice.text.toString().toDouble()
        post.description = productDescription.text.toString()
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

            } else {
                photoUri =  data.data!!
                productPhoto.setImageURI(photoUri)

                /*if (photoURI == null)
                    Toast.makeText(
                        this,
                        getString(R.string.no_image),
                        Toast.LENGTH_LONG).show()*/
            }
        }
    }

    fun getDataAsBytes(): ByteArray {
        // Get the data from an ImageView as bytes
        val bitmap = (productPhoto.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        return baos.toByteArray()
    }


    @SuppressLint("RestrictedApi")
    fun uploadPhotoAndPost() {
        val storageRef = FirebaseStorage.getInstance().reference
        val photoRef = storageRef.child(photoUri.toString())

        App.registerBroadcast(photoPathReceiver, IntentFilter(App.PHOTO_UPLOADED))

        photoRef.putBytes(getDataAsBytes())
            .continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation photoRef.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    with (App.appContext) {
                        Intent(App.PHOTO_UPLOADED).apply {
                            putExtra(App.PHOTO_PATH_EXTRA, task.result!!.toString())
                            App.sendBroadcast(this)
                        }
                    }
                }
            }
    }


}
