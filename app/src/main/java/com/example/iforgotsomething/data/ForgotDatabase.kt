package com.example.iforgotsomething.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.iforgotsomething.data.models.ForgotData
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized


@Database(entities = [ForgotData::class], version = 1, exportSchema = false)
//To now that converter class is only for convert -> whenever we insert data to database , it will convert it instantly
@TypeConverters(Converter::class)
abstract class ForgotDatabase: RoomDatabase() {

    abstract fun forgotDao(): ForgotDao

    //singleton like public static final in java
    companion object {

        // Immediately made visible for other threads
        @Volatile
        private var INSTANCE: ForgotDatabase? = null

        @InternalCoroutinesApi
        fun getDatabase(context: Context): ForgotDatabase{

            val tempInstance = INSTANCE

            if(tempInstance != null)
            {
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        ForgotDatabase::class.java,
                        "forgot_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }

    }

}