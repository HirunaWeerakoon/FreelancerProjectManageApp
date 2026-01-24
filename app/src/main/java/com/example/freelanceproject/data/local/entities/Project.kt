package com.example.freelanceproject.data.local.entities

import com.example.freelanceproject.data.model.ProjectType
import java.sql.Date

@Entity(tableName="projects")
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val projectType: ProjectType,
    val description: String,
    val clientID: Int,
    val targetAmount: Double,
    val paidAmount: Double,
    val totalHours: Double=0.0,
    val isCompleted: Boolean,
    val startDate: Date,
    val endDate: Date,
    val status: ProjectStatus = ProjectStatus.PLANNING
)
enum class ProjectStatus{
    PLANNING,
    ACTIVE,
    COMPLETED
}