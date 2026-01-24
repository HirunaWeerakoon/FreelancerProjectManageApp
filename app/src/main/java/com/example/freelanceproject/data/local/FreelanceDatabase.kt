package com.example.freelanceproject.data.local

import com.example.freelanceproject.data.local.dao.ClientDao
import com.example.freelanceproject.data.local.dao.ProjectDao

@Database(
    entities = [Project::class, Client::class],
    version = 1,
    exportSchema = false
)
abstract class FreelanceDatabase: RoomDatabase() {
    abstract val clientDao: ClientDao
    abstract val projectDao: ProjectDao

    companion object {
        @Volatile
        private var INSTANCE: FreelanceDatabase? = null

        fun getDatabase(context: Context): FreelanceDatabase {}

    }

}