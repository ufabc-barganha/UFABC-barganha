package br.edu.ufabc.ufabcbarganha.feed.categories.housing


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.core.content.ContextCompat
import br.edu.ufabc.ufabcbarganha.App
import br.edu.ufabc.ufabcbarganha.R
import br.edu.ufabc.ufabcbarganha.data.HousingDAO
import br.edu.ufabc.ufabcbarganha.feed.general.PostDetailActivity
import br.edu.ufabc.ufabcbarganha.model.Post
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_housing.*
import java.util.*

class HousingFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {

    companion object{
        private val LOCATION_PERMISSION_REQUEST_CODE = 6969
        private var permissionRequested = false

    }

    private lateinit var contextFrag: Context
    private lateinit var maps: GoogleMap
    private lateinit var housings : List<Post>
    private var selectedMarker : Marker? = null

    private lateinit var defaultMarkerIcon: BitmapDescriptor
    private lateinit var selectedMarkerIcon: BitmapDescriptor

    private var housingContainerSetuped = false


    //----------------------------------------- Initialization -----------------------------------------------//
    //--------------------------------------------------------------------------------------------------------//

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.contextFrag = context
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_housing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        //Setup fragment
        childFragmentManager.beginTransaction().replace(R.id.fragment_maps, SupportMapFragment()).commitNow()
        (childFragmentManager.findFragmentById(R.id.fragment_maps) as SupportMapFragment).getMapAsync(this)
        //Setup layout
        val layoutListener = ViewTreeObserver.OnGlobalLayoutListener{
            if(!housingContainerSetuped) {
                container_housing_info.translationY = container_housing_info.height.toFloat()
                housingContainerSetuped = true
            }
        }
        container_housing_info.viewTreeObserver.addOnGlobalLayoutListener (layoutListener)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.maps = googleMap
        maps.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-23.644926, -46.527625), 16f)) //UFABC SANTo ANDRE
        funEnableMapLocation()

        //callbacks
        maps.setOnMarkerClickListener(this)
        maps.setOnMapClickListener(this)
        //
        setupMarkerIcons()
        //
        loadHousings()
    }

    fun setupMarkerIcons(){
        defaultMarkerIcon = BitmapDescriptorFactory.defaultMarker()
        selectedMarkerIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)
    }

    //---------------------------------------------- MAP -----------------------------------------------------//
    //--------------------------------------------------------------------------------------------------------//

    fun createHousingMarkers(){
        //for( post: Post in housings){
        for (i in 0 until housings.size){
            var marker = maps.addMarker(MarkerOptions().position(housings[i].latLng!!))
            housings[i].id = i
            marker.tag = housings[i]
        }
    }

    override fun onMapClick(pos: LatLng?) {
        selectMarker(null)
    }

    override fun onMarkerClick(m: Marker?): Boolean {
        selectMarker(m)
        return true
    }

    private fun selectMarker(m : Marker?){
        if (m == selectedMarker)
            return
        //
        if(selectedMarker != null){
            selectedMarker?.setIcon(defaultMarkerIcon)

            if(m == null) {
                container_housing_info.animate().setDuration(200L).translationY(container_housing_info.height.toFloat()).start()
            }
        }
        selectedMarker = m
        if(selectedMarker != null){
            selectedMarker?.setIcon(selectedMarkerIcon)

            // setup housing info container
            val post = m!!.tag as Post
            tv_housing_title.text = post.productName
            tv_housing_description.text = post.description
            Glide.with(this).load(post.photo).placeholder(null).error(R.drawable.ic_image_not_found).into(iv_housing_photo)
            if(container_housing_info.translationY != 0f) {
                //container_housing_info.animate().setDuration(200L).translationY(0f).start()
                val intent = Intent(context, PostDetailActivity::class.java)
                intent.putExtra(App.HOUSING_POSITION, post.id)
                ContextCompat.startActivity(contextFrag, intent, null)
            }
        }
    }

    //----------------------------------------------- Data ---------------------------------------------------//
    //--------------------------------------------------------------------------------------------------------//

    fun loadHousings(){
        this.housings = testHousings()
        createHousingMarkers()
    }

    fun testHousings() : List<Post> {
        val testHousings = mutableListOf<Post>()
        val housingDaoInst = HousingDAO.instance
        for (i in 0 until housingDaoInst.size())
            testHousings.add(housingDaoInst.getItemAt(i))

        return testHousings
    }


    //------------------------------------- Maps Location Auxiliar -------------------------------------------//
    //--------------------------------------------------------------------------------------------------------//
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        funEnableMapLocation()
    }

    private fun requestLocationPermission(){
        permissionRequested = true
        requestPermissions( arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun funEnableMapLocation(){
        if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if(!permissionRequested){
                requestLocationPermission()
            }
            return
        }

        maps.isMyLocationEnabled = true
    }
}
