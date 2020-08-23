package com.frolfr.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    version = 1,
    exportSchema = false,
    entities = [
        CourseEntity::class,
        RoundEntity::class,
        RoundHoleEntity::class,
        TurnEntity::class,
        UserEntity::class,
        UserScorecardEntity::class
    ]
)
abstract class FrolfrDatabase : RoomDatabase() {
    abstract val roundDAO: RoundDAO
    abstract val courseDAO: CourseDAO

    companion object {
        @Volatile
        private var INSTANCE: FrolfrDatabase? = null

        fun getInstance(context: Context): FrolfrDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FrolfrDatabase::class.java,
                        "frolfr"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }

                return instance
            }
        }
    }
}