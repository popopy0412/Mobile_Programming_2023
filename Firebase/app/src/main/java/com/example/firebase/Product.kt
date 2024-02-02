package com.example.firebase

data class Product(var pid: Int, var pName: String, var pQuantity: Int){
    constructor() : this(0, "noinfo", 0)
}
