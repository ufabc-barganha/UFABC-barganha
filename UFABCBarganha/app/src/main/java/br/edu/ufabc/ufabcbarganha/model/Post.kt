package br.edu.ufabc.ufabcbarganha.model

import com.google.android.gms.maps.model.LatLng

data class Post(
        val username: String,
        val productName: String,
        val photo: String,
        val price: Double,
        val description: String) {

        var latLng : LatLng? = null
}