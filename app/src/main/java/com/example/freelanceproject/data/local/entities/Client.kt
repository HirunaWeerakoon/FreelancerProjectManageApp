package com.example.freelanceproject.data.local.entities

@Entity(tableName = "clients")
data class Client(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val email: String?,
    val phone: String?,
    val address: String?,
    val companyName: String?,
)