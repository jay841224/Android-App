package com.example.criminalintent.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.criminalintent.CrimeRepository
import com.example.criminalintent.data.Crime
import java.util.UUID

class CrimeDetailViewModel : ViewModel() {

    private val crimeRepository = CrimeRepository.get()

    // MutableLiveData 基本就是 LiveData，只是去覆寫讓我們可以去改變 value
    private val crimeIdLiveData = MutableLiveData<UUID>()

    // Transformations 只有在 LiveData 有變化時執行
    val crimeLiveData: LiveData<Crime> =
        Transformations.switchMap(crimeIdLiveData) { crimeId ->
            crimeRepository.getCrime(crimeId)
        }

    /**
     * 改變 crimeIdLiveData 去觸發 Transformations 重新去查詢 db
     */
    fun loadCrime(crimeId: UUID) {
        crimeIdLiveData.value = crimeId
    }

    /**
     * 更新資料庫
     */
    fun saveCrime(crime: Crime) {
        crimeRepository.updateCrime(crime)
    }
}