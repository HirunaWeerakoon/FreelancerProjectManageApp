package com.example.freelanceproject.data.local.dao

import androidx.room.*

import com.example.freelanceproject.data.local.entities.Project
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: Project)

    @Delete
    suspend fun deleteProject(project: Project)

    @Update
    suspend fun updateProject(project: Project)

    @Query("SELECT * FROM projects")
    fun getAllProjects(): Flow<List<Project>>

}



