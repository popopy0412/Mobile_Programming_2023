package com.example.bobfairy

import com.google.android.gms.maps.model.LatLng

data class apiResponse(
    val results: ArrayList<PlaceData>
)

data class PlaceData(
    val name: String,
    val geometry: geometry?,
    val rating: Float,
    val user_ratings_total: Int,
    val photos: List<photo>?,
    val place_id: String,
    var is_like: Boolean = false
){
    constructor():this("noinfo", null, 0.0f, 0, null, "noinfo", false)
    //꼭 디폴트 생성자 추가!
}

data class geometry(
    val location: location?
){
    constructor():this(null)
}

data class location(
    val lat: Double,
    val lng: Double
){
    constructor():this(0.0, 0.0)
}

data class photo(
    val photo_reference: String
){
    constructor():this("")
}

data class query(
    val radius: Int,
    val lat: Double,
    val lng: Double,
    val keyword: String
) :java.io.Serializable

data class apiDetailResponse(
    val result: placeDetail
)

data class placeDetail(
    val url: String
)