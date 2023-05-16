package com.example.roomdb

import androidx.room.*

@Dao
interface ProductDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertProduct(product: Product)

    @Delete
    fun deleteProduct(product: Product)

    @Update
    fun updateProduct(product: Product)

    @Query("SELECT * FROM products")
    fun getAllRecord(): List<Product>

    @Query("SELECT * FROM products WHERE pname = :name")
    fun findProduct(name: String): List<Product>

    @Query("SELECT * FROM products where pname like :name")
    fun findProduct2(name: String): List<Product>
}