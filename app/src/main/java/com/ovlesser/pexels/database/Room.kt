package com.ovlesser.pexels.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface PexelsPhotoDao {
    @Query("select * from databasepexelsphoto")
    fun getPhotos(): LiveData<List<DatabasePexelsPhoto>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(photos: List<DatabasePexelsPhoto>)

    @Query("delete from databasepexelsphoto")
    fun clearAll()
}

@Database(entities = [DatabasePexelsPhoto::class], version = 1)
abstract class PexelsPhotoDatabase: RoomDatabase() {
    abstract val pexelsPhotoDao: PexelsPhotoDao
}

private lateinit var INSTANCE: PexelsPhotoDatabase

fun getDatabase(context: Context): PexelsPhotoDatabase {
    synchronized(PexelsPhotoDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                PexelsPhotoDatabase::class.java,
                "pexelsphoto").build()
        }
    }
    return INSTANCE
}