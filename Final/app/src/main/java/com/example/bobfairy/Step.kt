package com.example.bobfairy

data class Step(val step: String, val description: String, val image: String) : java.io.Serializable{
    constructor() : this("noinfo", "noinfo", "noinfo")
}
