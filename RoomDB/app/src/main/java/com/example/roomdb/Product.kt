package com.example.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true) var pId: Int,
    @ColumnInfo(name = "pname") var pName: String,
    @ColumnInfo(name = "pquantity") var pQuantity: Int)
