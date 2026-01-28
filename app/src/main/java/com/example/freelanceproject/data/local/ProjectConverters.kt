package com.example.freelanceproject.data.local

import androidx.room.TypeConverter
import com.example.freelanceproject.data.local.entities.ProjectStatus
import com.example.freelanceproject.data.model.ProjectType
import java.util.Date

class ProjectConverters {

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun fromStatus(status: ProjectStatus): String {
        return status.name
    }

    @TypeConverter
    fun toStatus(value: String): ProjectStatus {
        return ProjectStatus.valueOf(value)
    }

    @TypeConverter
    fun fromProjectType(type: ProjectType): String {
        return type.name
    }

    @TypeConverter
    fun toProjectType(value: String): ProjectType {
        return ProjectType.valueOf(value)
    }
}