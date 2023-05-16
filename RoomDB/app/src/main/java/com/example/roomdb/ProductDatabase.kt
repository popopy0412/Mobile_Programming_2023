package com.example.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Product::class],
    version = 1
)

abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDAO

    companion object{
        private var INSTANCE: ProductDatabase? = null

        fun getDatabase(context: Context): ProductDatabase{
            val tempInstance = INSTANCE

            if(tempInstance != null){
                return tempInstance
            }
            val instance = Room.databaseBuilder(
                context,
                ProductDatabase::class.java,
                "productdb"
            ).build()

            INSTANCE = instance
            return instance
        }
    }
}