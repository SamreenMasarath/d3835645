package com.example.alertguard

import com.example.alertguard.db.models.UserModel

object Singleton {

    var userModel: UserModel? = null
    const val login: String = "Login"
    const val userId: String = "UserID"

}