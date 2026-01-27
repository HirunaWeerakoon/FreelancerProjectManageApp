package com.example.freelanceproject.data.repository

import com.example.freelanceproject.data.local.dao.ClientDao
import com.example.freelanceproject.data.local.entities.Client
import kotlinx.coroutines.flow.Flow

class ClientRepository(private val clientDao: ClientDao) {
    val allClients: Flow<List<Client>> = clientDao.getAllClients()

    suspend fun insert(client: Client) {
        clientDao.insertClient(client)
    }
    suspend fun delete(client: Client) {
        clientDao.deleteClient(client)
    }
    suspend fun update(client: Client) {
        clientDao.updateClient(client)
    }



}
