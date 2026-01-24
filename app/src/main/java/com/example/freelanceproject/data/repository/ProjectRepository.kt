package com.example.freelanceproject.data.repository

import com.example.freelanceproject.data.local.dao.ProjectDao
import com.example.freelanceproject.data.local.entities.Project
import kotlinx.coroutines.flow.Flow

class ProjectRepository ( private val projectDao: ProjectDao) {

    val allProjects: Flow<List<Project>> = projectDao.getAllProjects()

    fun getAllProjects(): Flow<List<Project>> {
        return projectDao.getAllProjects()
    }

    fun getProjectById(id: Int): Flow<List<Project>> {
        return projectDao.getProjectById(id)
    }

    suspend fun insert(project: Project) {
        projectDao.insertProject(project)
    }

    suspend fun delete(project: Project) {
        projectDao.deleteProject(project)
    }

    suspend fun update(project: Project) {
        projectDao.updateProject(project)
    }

}