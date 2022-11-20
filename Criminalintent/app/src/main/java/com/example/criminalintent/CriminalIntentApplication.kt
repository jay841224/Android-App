package com.example.criminalintent

import android.app.Application

/**
 * 用來初始化 DataBase Repository，這樣一來，就可以取用 Database
 * Application 在一開始就會建立，且不會像是 Activity 和 Fragment 會經常被銷毀和重建
 */
class CriminalIntentApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        CrimeRepository.initialize(this)
    }
}