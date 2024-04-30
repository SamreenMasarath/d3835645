package com.example.alertguard.db

import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase
import com.example.alertguard.AppController
import com.example.alertguard.R
import com.example.alertguard.db.dao.ReportDataDAO
import com.example.alertguard.db.dao.UserModelDAO
import com.example.alertguard.db.models.ReportDataModel
import com.example.alertguard.db.models.UserModel

@Database(entities = [UserModel::class, ReportDataModel::class], version = 1)
abstract class AppDatabase: RoomDatabase() {

    abstract fun userModelDao(): UserModelDAO
    abstract fun reportDataDao(): ReportDataDAO

    companion object {
        private var database: AppDatabase? = null
        fun getDatabase(): AppDatabase {
            if (database == null) {
                database = databaseBuilder(
                    AppController.context, AppDatabase::class.java,
                    AppController.context.getString(R.string.app_name)
                ).allowMainThreadQueries().fallbackToDestructiveMigration().build()
            }
            return database!!
        }
    }

}