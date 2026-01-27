package com.example.freelanceproject.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.freelanceproject.data.local.dao.ClientDao
import com.example.freelanceproject.data.local.dao.ProjectDao
import com.example.freelanceproject.data.local.entities.Client
import com.example.freelanceproject.data.local.entities.Project

@Database(entities = [Project::class, Client::class],version = 2,exportSchema = false)

abstract class FreelanceDatabase: RoomDatabase() {
    abstract val clientDao: ClientDao
    abstract val projectDao: ProjectDao

    companion object {
        @Volatile
        private var INSTANCE: FreelanceDatabase? = null

        fun getDatabase(context: Context): FreelanceDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FreelanceDatabase::class.java,
                    "freelance_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}