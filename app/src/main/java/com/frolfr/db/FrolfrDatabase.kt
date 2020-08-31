package com.frolfr.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.frolfr.db.dao.*
import com.frolfr.db.model.*

@Database(
    version = 6,
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
    abstract val userDAO: UserDAO
    abstract val userScorecardDAO: UserScorecardDAO
    abstract val turnDAO: TurnDAO

    companion object {
        @Volatile
        private var INSTANCE: FrolfrDatabase? = null

        fun init(context: Context) {
            synchronized(this) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        FrolfrDatabase::class.java,
                        "frolfr"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
        }

        fun getInstance(context: Context): FrolfrDatabase {
            if (INSTANCE == null) {
                if (context == null) {
                    throw Exception(
                        "Cannot call getInstance without first calling init() or passing a context"
                    )
                }
                init(context)
            }
            return INSTANCE!!
        }

        fun getInstance(): FrolfrDatabase {
            if (INSTANCE == null) {
                throw Exception(
                    "Cannot call getInstance without first calling init() or passing a context"
                )
            }
            return INSTANCE!!
        }
    }
}