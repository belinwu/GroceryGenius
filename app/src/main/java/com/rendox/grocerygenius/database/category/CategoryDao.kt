package com.rendox.grocerygenius.database.category

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Upsert
    suspend fun upsertCategories(categories: List<CategoryEntity>)

    @Query("SELECT * FROM CategoryEntity")
    fun getAllCategories(): Flow<List<CategoryEntity>>

    @Query(
        """
            DELETE FROM CategoryEntity
            WHERE id in (:ids)
        """,
    )
    suspend fun deleteCategories(ids: List<String>)
}