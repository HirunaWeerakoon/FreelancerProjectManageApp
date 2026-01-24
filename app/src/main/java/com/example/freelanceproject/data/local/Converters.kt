package com.example.freelanceproject.data.local

import androidx.room.TypeConverter
import com.example.freelanceproject.data.local.entities.ProjectStatus
import com.example.freelanceproject.data.model.ProjectType
import java.util.Date

/**
 * Type converters to allow Room to reference complex data types.
 */
class TypeConverters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromProjectType(value: String): ProjectType {
        return ProjectType.valueOf(value)
    }

    @TypeConverter
    fun projectTypeToString(projectType: ProjectType): String {
        return projectType.name
    }

    @TypeConverter
    fun fromProjectStatus(value: String): ProjectStatus {
        return ProjectStatus.valueOf(value)
    }

    @TypeConverter
    fun projectStatusToString(projectStatus: ProjectStatus): String {
        return projectStatus.name
    }
}
