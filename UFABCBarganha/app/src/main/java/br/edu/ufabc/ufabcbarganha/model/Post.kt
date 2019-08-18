package br.edu.ufabc.ufabcbarganha.model

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable
import java.util.*

class Post : Serializable {

        enum class PostType { FOOD, HOUSING, PRODUCT }

        constructor()

        constructor(username: String, productName: String, photo: String, price: Double, description: String, postTime: Date, postType: PostType){
                this.username = username
                this.productName = productName
                this.photo = photo
                this.price = price
                this.description = description
                this.postTime = postTime
                this.postType = postType
        }

        var id: String = ""
        var postType: PostType = PostType.PRODUCT
        var username: String = ""
        var productName: String = ""
        var photo: String = ""
        var price: Double = 0.0
        var description: String = ""
        var postTime: Date = Date(System.currentTimeMillis())

        // Only Housing
        var lat: Double? = null
        var lng: Double? = null

        fun checkHasLocation(): Boolean{
                return lat != null && lng != null
        }

        fun retrieveLatLng(): LatLng?{
                if(!checkHasLocation())
                        return null
                return LatLng(lat!!, lng!!)
        }

}