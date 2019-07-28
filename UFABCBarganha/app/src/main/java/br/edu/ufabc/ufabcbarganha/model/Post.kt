package br.edu.ufabc.ufabcbarganha.model

import com.google.android.gms.maps.model.LatLng
import java.util.*

data class Post(
        val username: String,
        val productName: String,
        val photo: String,
        val price: Double,
        val description: String,
        val postTime: Date) {

        var latLng : LatLng? = null
        var id: Int = 0
}