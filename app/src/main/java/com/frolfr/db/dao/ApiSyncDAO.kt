package com.frolfr.db.dao

import androidx.room.*
import com.frolfr.db.model.ApiSyncEntity
import com.frolfr.db.model.EntityType
import com.frolfr.db.model.EntityTypeToStringConverter

@Dao
@TypeConverters(EntityTypeToStringConverter::class)
interface ApiSyncDAO {
    @Query("SELECT * FROM ApiSync WHERE entityType = :entityType")
    fun get(entityType: EntityType): ApiSyncEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(apiSync: ApiSyncEntity)
}