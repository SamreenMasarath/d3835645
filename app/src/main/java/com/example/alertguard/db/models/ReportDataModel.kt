package com.example.alertguard.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("ReportData")
data class ReportDataModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    @ColumnInfo(name = "email")
    var email: String = "",
    @ColumnInfo(name = "contact")
    var contact: String = "",
    @ColumnInfo(name = "latitude")
    var latitude: String = "",
    @ColumnInfo(name = "longitude")
    var longitude: String = "",
)