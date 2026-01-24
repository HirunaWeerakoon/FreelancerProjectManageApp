package com.example.freelanceproject.data.local.dao

import com.example.freelanceproject.data.local.entities.Client
import kotlinx.coroutines.flow.Flow

@Dao
interface ClientDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClient(client: Client)

    @Delete
    suspend fun deleteClient(client: Client)

    @Update
    suspend fun updateClient(client: Client)

    @Query("SELECT * FROM clients")
    fun getAllClients(): Flow<List<Client>>
}