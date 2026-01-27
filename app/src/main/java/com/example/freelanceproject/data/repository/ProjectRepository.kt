package com.example.freelanceproject.data.repository

import com.example.freelanceproject.data.local.dao.ProjectDao
import com.example.freelanceproject.data.local.entities.Project
import kotlinx.coroutines.flow.Flow

class ProjectRepository ( private val projectDao: ProjectDao) {

    val allProjects: Flow<List<Project>> = projectDao.getAllProjects()


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