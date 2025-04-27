package com.larrykin.notificationhub.core.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.larrykin.notificationhub.core.data.entities.NotificationHistoryItem
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationHistoryItemDao {

    // insert a notification item
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryItem(item: NotificationHistoryItem)

    // get all notification items
    @Query("Select * from notification_history")
    fun getAllHistory(): Flow<List<NotificationHistoryItem>>
}