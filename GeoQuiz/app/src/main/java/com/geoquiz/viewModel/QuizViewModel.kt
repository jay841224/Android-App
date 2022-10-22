package com.geoquiz.viewModel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.geoquiz.bank.Question

class QuizViewModel : ViewModel() {

    companion object {
        private const val TAG = "QuizViewModel"
    }

    /**
     * 問題庫
     */
    private val questionBank = listOf<Question>(
        Question("問題一", true),
        Question("問題二", false),
        Question("問題三", false),
        Question("問題四", true)
    )

    /**
     * 題目index
     */
    var currentIndex = 0

    /**
     * 記錄答案對錯
     */
    private var answers = arrayOfNulls<Boolean>(questionBank.size)

    /**
     * 總分
     */
    var score = 0

    var isCheat = false

    fun getQuestionText(): String {
        return questionBank[currentIndex].question;
    }

    /**
     * 下一題
     */
    fun nextQuestion(): Boolean {
        currentIndex++
        // 重新開始
        if (isLast()) {
            score = answers.count { b -> b == true }.times(100 / questionBank.size)
            currentIndex = 0
            answers = arrayOfNulls<Boolean>(questionBank.size)
            isCheat = false
            return true
        }
        return false
    }

    private fun isLast(): Boolean {
        return currentIndex == questionBank.size
    }

    /**
     * 上一題
     */
    fun prevQuestion() {
        currentIndex--
    }


    /**
     * 檢查答案
     */
    fun checkAnswer(answer: Boolean) {
        answers[currentIndex] = answer == questionBank[currentIndex].answer
    }

    fun getCurrentAnswer(): Boolean {
        return questionBank[currentIndex].answer
    }

//    init {
//        Log.d(TAG, "ViewModel 建立")
//    }
//
//    /**
//     * ViewModel 銷毀前
//     */
//    override fun onCleared() {
//        super.onCleared()
//        Log.d(TAG, "ViewModel 準備銷毀")
//    }
}