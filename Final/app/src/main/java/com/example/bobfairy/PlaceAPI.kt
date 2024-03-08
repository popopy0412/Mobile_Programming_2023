package com.example.bobfairy

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface PlaceAPI {
    @GET("api/place/nearbysearch/json")
    fun getPlace(@QueryMap par: Map<String, String>): Call<apiResponse>

    @GET("api/place/details/json")
    fun getPlaceDetail(@QueryMap par: Map<String, String>): Call<apiDetailResponse>

//https://maps.googleapis.com/maps/api/place/details/json?place_id=ChIJ05IRjKHxEQ0RJLV_5NLdK2w&fields=place_id&key=AIzaSyBD9_z2GysLxXN-hVEmJsrhbROCw1rvbiY

//    @GET("api/place/photo")
//    fun getImg(@QueryMap par: Map<String, String>)
//    https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=AZose0m8LS6ZggWOV4FFo6LQ_HiIgHg5VcFWCcv9Lo_R0vo7A5iZS0PSisQrxFguKYEXQMDQ-gC_4iNTs4uKyR0LiQ3nsOtP3Pqql0f_fT8Ch6sfuLDR8xQ3zlMLfrJTA6TBtMJ9qyIi8hiSvJOWrLeXfXNzr3BvSsaTHLnOGTLG8wpEhuK9&key=AIzaSyBD9_z2GysLxXN-hVEmJsrhbROCw1rvbiY
//    https://lh3.googleusercontent.com/places/ANJU3DsbcBgU3FnOcbzZvp-Ichf8jRjsZYmUQ4mM13P6Zr1K67MDcYaqV01DkKuU8aC-DEmqBqRNQUCs4fAQVEWldx7wEuQe4SC_V-A=s1600-w400
}

