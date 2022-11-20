package com.example.criminalintent

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.example.criminalintent.data.Crime
import com.example.criminalintent.database.CrimeDatabase
import java.util.UUID
import java.util.concurrent.Executors

/**
 * repository pattern
 */
class CrimeRepository(context: Context) {

    companion object {

        private var INSTANCE: CrimeRepository? = null

        private const val DATABASE_NAME = "crime-database"

        /**
         * 單例
         */
        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = CrimeRepository(context)
            }
        }

        fun get(): CrimeRepository {
            return INSTANCE ?: throw IllegalStateException("CrimeRepository must be initialized")
        }
    }

    private val database: CrimeDatabase = Room.databaseBuilder(
        context.applicationContext,
        CrimeDatabase::class.java,
        DATABASE_NAME
    ).build()

    private val crimeDao = database.crimeDao()

    private val executor = Executors.newSingleThreadExecutor()

    fun getCrimes(): LiveData<List<Crime>> = crimeDao.getCrimes()

    fun getCrime(uuid: UUID): LiveData<Crime?> = crimeDao.getCrime(uuid)

    fun insertCrime(crime: Crime) = executor.execute { crimeDao.insert(crime) }

    fun clearAll() = executor.execute { crimeDao.clearAll() }

    fun updateCrime(crime: Crime) = executor.execute { crimeDao.updateCrime(crime) }
}