package com.example.freelanceproject.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.freelanceproject.data.model.ProjectType
import java.util.Date

@Entity(tableName="projects")
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val projectType: ProjectType,
    val description: String?,
    val clientID: Int?,
    val targetAmount: Double,
    val paidAmount: Double,
    val totalHours: Double?,
    val isCompleted: Boolean,
    val startDate: Date?,
    val endDate: Date?,
    val status: ProjectStatus = ProjectStatus.PLANNING
)
enum class ProjectStatus{
    PLANNING,
    ACTIVE,
    COMPLETED
}