package com.example.freelanceproject.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.freelanceproject.data.local.ProjectConverters
import com.example.freelanceproject.data.model.ProjectType
import java.util.Date

@Entity(tableName="projects")
@TypeConverters(ProjectConverters::class)
data class Project(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val projectType: ProjectType,
    val description: String?,
    val clientID: String?,
    val targetAmount: Double,
    val targetHours: Double?, // New Field
    val paidAmount: Double,
    val totalHours: Double?,
    val isCompleted: Boolean,
    val startDate: Date?,
    val endDate: Date?,
    val status: ProjectStatus = ProjectStatus.PLANNING,
    val timerStartTime: Long? = null

)
enum class ProjectStatus {
    PLANNING,
    ACTIVE,
    ADVANCED,
    PAID,
    COMPLETED,
    CANCELLED
}