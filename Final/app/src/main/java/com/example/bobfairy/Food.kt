package com.example.bobfairy

data class Food(val name: String, var type: String, var cal: String, var image: String, var ingredient: String, var bookmark: Boolean, var steps: ArrayList<Step>) : java.io.Serializable{
    constructor() : this("noinfo", "noinfo", "noinfo", "noinfo", "noinfo", false, ArrayList())
}
