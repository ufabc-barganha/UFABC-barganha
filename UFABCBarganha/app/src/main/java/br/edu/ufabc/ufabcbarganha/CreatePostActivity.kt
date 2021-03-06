package br.edu.ufabc.ufabcbarganha

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.ProgressDialog
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
import com.google.android.material.snackbar.Snackbar

import kotlinx.android.synthetic.main.activity_create_post.*
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.view.View
import androidx.core.content.ContextCompat
import br.edu.ufabc.ufabcbarganha.data.firestore.FirestoreDatabaseOperationListener
import br.edu.ufabc.ufabcbarganha.data.firestore.PostDAO
import br.edu.ufabc.ufabcbarganha.feed.general.FeedActivity
import br.edu.ufabc.ufabcbarganha.map.WorkaroundMapFragment
import br.edu.ufabc.ufabcbarganha.model.Post
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import br.edu.ufabc.ufabcbarganha.user.data.FirebaseUserHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import kotlinx.android.synthetic.main.cardview_create_post.*
import java.io.ByteArrayOutputStream
import java.util.*

class CreatePostActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMapClickListener, AdapterView.OnItemSelectedListener {

    private companion object Constants {
        const val CAMERA_REQUEST_PERMISSIONS = 100
        const val IMAGE_CAPTURE_INTENT = 200
    }

    private lateinit var productTitle: EditText
    private lateinit var productPrice: EditText
    private lateinit var productDescription: MultiAutoCompleteTextView
    private lateinit var addPhoto: Button
    private lateinit var createPost: Button
    private lateinit var productPhoto: ImageView
    private lateinit var postType: Spinner
    private lateinit var phone: EditText

    private lateinit var photoUri: Uri
    private var isPhotoDone: Boolean = false

    private lateinit var maps: GoogleMap
    private var positionMarker: Marker? = null

    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)
        setSupportActionBar(toolbar)

        initializeViews()
        setListeners()

        if(intent.hasExtra(App.POST_TYPE_EXTRA))
            postType.setSelection((postType.adapter as ArrayAdapter<Post.PostType>)
                .getPosition(intent.extras?.get(App.POST_TYPE_EXTRA) as Post.PostType))

        var mapFragmentManager = WorkaroundMapFragment()
        mapFragmentManager.setListener { ScrollView01.requestDisallowInterceptTouchEvent(true)}
        supportFragmentManager.beginTransaction().replace(R.id.fragment_maps, mapFragmentManager).commitNow()
        (supportFragmentManager.findFragmentById(R.id.fragment_maps) as SupportMapFragment).getMapAsync(this)

        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Enviando...")
        progressDialog.setCancelable(false)
        progressDialog.setInverseBackgroundForced(false)
    }

    private fun initializeViews() {
        productTitle = findViewById(R.id.create_post_title)
        productPrice = findViewById(R.id.create_post_price)
        phone = findViewById(R.id.create_post_phone)
        productDescription = findViewById(R.id.create_post_description)
        addPhoto = findViewById(R.id.add_photo_button)
        createPost = findViewById(R.id.create_post_button)
        productPhoto = findViewById(R.id.product_photo_imageview)
        postType = findViewById(R.id.post_type)


        postType.adapter = ArrayAdapter(
            this,
            R.layout.spinner_layout,
            Post.PostType.values())

        postType.onItemSelectedListener = this
    }

    private fun setListeners() {
        toolbar.setNavigationOnClickListener {
            finish()
        }

        addPhoto.setOnClickListener{

            val permissions = checkPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
            if(permissions)
                callCamera()
            else
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), CAMERA_REQUEST_PERMISSIONS)
        }

        createPost.setOnClickListener{
            if (productTitle.text.toString().isEmpty() || productPrice.text.toString().isEmpty() || phone.text.toString().isEmpty() ||
                    productDescription.text.toString().isEmpty() || !isPhotoDone) {
                Toast.makeText(this@CreatePostActivity, R.string.create_post_missing_fields, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (postType.selectedItem == Post.PostType.HOUSING && positionMarker == null ){
                Toast.makeText(this@CreatePostActivity, R.string.create_post_missing_location, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            progressDialog.show()
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
                        Toast.makeText(this@CreatePostActivity.applicationContext, R.string.create_post_success, Toast.LENGTH_LONG).show()
                        progressDialog.dismiss()
                        val intent = Intent(this@CreatePostActivity, FeedActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)

                    }

                    override fun onFailure(e: Exception) {
                        Toast.makeText(this@CreatePostActivity, R.string.create_post_failure, Toast.LENGTH_LONG).show()
                        progressDialog.dismiss()
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
        post.phone = phone.text.toString()

        if(postType.selectedItem == Post.PostType.HOUSING){
            post.lat = positionMarker!!.position.latitude
            post.lng = positionMarker!!.position.longitude
        }

        return post
    }

    @TargetApi(Build.VERSION_CODES.M)
    private fun checkPermissions(permissionCamera: String, permissionExternalStorage: String): Boolean {
        return checkSelfPermission(permissionCamera) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(permissionExternalStorage) == PackageManager.PERMISSION_GRANTED
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
            isPhotoDone = true

            if (data?.hasExtra("data") as Boolean) {
                val photo = data.extras?.get("data") as Bitmap

                productPhoto.scaleType = ImageView.ScaleType.CENTER_CROP
                productPhoto.setImageBitmap(photo)

                photoUri = getImageUri(photo)

            } else {
                photoUri =  data.data!!
                productPhoto.setImageURI(photoUri)
            }
        }
    }

    private fun getDataAsBytes(): ByteArray {
        // Get the data from an ImageView as bytes
        val bitmap = (productPhoto.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

        return baos.toByteArray()
    }

    private fun getImageUri(image: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, bytes)

        val path = MediaStore.Images.Media.insertImage(this.contentResolver, image, "Title", null)

        return Uri.parse(path)
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

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, pos: Int, id: Long) {
        if(parent!!.getItemAtPosition(pos).equals(Post.PostType.HOUSING)){
            fragment_maps.visibility = View.VISIBLE
        } else{
            fragment_maps.visibility = View.GONE
        }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    //------------------------------------- Map -------------------------------------------//
    //--------------------------------------------------------------------------------------------------------//
    override fun onMapReady(googleMap: GoogleMap?) {
        this.maps = googleMap!!
        maps.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-23.644926, -46.527625), 16f)) //UFABC SANTo ANDRE
        funEnableMapLocation()

        //callbacks
        maps.setOnMapClickListener(this)
    }

    override fun onMapClick(pos: LatLng?) {
        if(positionMarker == null){
            positionMarker = maps.addMarker(MarkerOptions().position(pos!!))
        }
        else{
            positionMarker!!.position = pos
        }
    }

    //------------------------------------- Maps Location Auxiliar -------------------------------------------//
    //--------------------------------------------------------------------------------------------------------//
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        funEnableMapLocation()
    }



    private fun funEnableMapLocation(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        maps.isMyLocationEnabled = true
    }


}
