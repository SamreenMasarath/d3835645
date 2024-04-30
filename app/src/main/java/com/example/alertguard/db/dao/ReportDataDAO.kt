package com.example.alertguard.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.alertguard.db.models.ReportDataModel

@Dao
interface ReportDataDAO {

    @Insert
    fun insert(reportDataModel: ReportDataModel)

    @Query("Select * from reportdata where email =:email")
    fun getReportData(email: String): List<ReportDataModel>?

}