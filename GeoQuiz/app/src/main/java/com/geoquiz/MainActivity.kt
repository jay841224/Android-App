package com.geoquiz

import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.ViewModelProvider
import com.geoquiz.viewModel.QuizViewModel

class MainActivity : AppCompatActivity() {

    /**
     * 這邊放const field，類似java的static
     */
    companion object {
        private const val TAG = "MainActivity"
        private const val KEY_INDEX = "KeyIndex"
    }

    // 綁定 ViewModel
    private val quizViewModel: QuizViewModel by lazy {
        ViewModelProvider(this).get(QuizViewModel::class.java)
    }

    /**
     * 畫面物件
     */
    private lateinit var trueBtn: Button
    private lateinit var falseBtn: Button
    private lateinit var nextBtn: ImageButton
    private lateinit var prevBtn: ImageButton
    private lateinit var questionText: TextView
    private lateinit var cheatBtn: Button

    private lateinit var launcher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(Companion.TAG, "呼叫 onCreate")

        trueBtn = findViewById(R.id.true_btn)
        falseBtn = findViewById(R.id.false_btn)
        nextBtn = findViewById(R.id.next_btn)
        prevBtn = findViewById(R.id.prev_btn)
        questionText = findViewById(R.id.question_text_view)
        cheatBtn = findViewById(R.id.cheat_btn)

        createLauncher()
        setClick()

        questionText.text = quizViewModel.getQuestionText()
    }

    override fun onStart() {
        super.onStart()
        Log.d(Companion.TAG, "呼叫 onStart()")
    }

    override fun onResume() {
        super.onResume()
        Log.d(Companion.TAG, "呼叫 onResume()")
    }

    override fun onPause() {
        super.onPause()
        Log.d(Companion.TAG, "呼叫 onPause()")
    }

    override fun onStop() {
        super.onStop()
        Log.d(Companion.TAG, "呼叫 onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(Companion.TAG, "呼叫 onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(Companion.TAG, "呼叫 onSaveInstanceState")

        // 將數據保存在Bundle中
        outState.putInt(Companion.KEY_INDEX, quizViewModel.currentIndex)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(Companion.TAG, "呼叫 onRestoreInstanceState")

        // 如果Bundle裡面有index就拿出來用
        quizViewModel.currentIndex =
            savedInstanceState?.getInt(Companion.KEY_INDEX) ?: 0 // ?: 如果前面是null就用：的值
    }

    /**
     * 設定click listener
     */
    private fun setClick() {
        trueBtn.setOnClickListener {
            quizViewModel.checkAnswer(true)
            nextQuestion()
        }

        falseBtn.setOnClickListener {
            quizViewModel.checkAnswer(false)
            nextQuestion()
        }

        nextBtn.setOnClickListener {
            nextQuestion()
        }

        prevBtn.setOnClickListener {
            quizViewModel.prevQuestion()
            questionText.text = quizViewModel.getQuestionText()
        }

        cheatBtn.setOnClickListener {
            var currentAnswer = quizViewModel.getCurrentAnswer()
            var intent = CheatActivity.newIntent(this@MainActivity, currentAnswer)
            // 動畫
            val option = ActivityOptionsCompat.makeClipRevealAnimation(it, 0, 0, it.width, it.height)

            // 跳轉頁面
            launcher.launch(intent, option)
//            launcher.launch(intent)
        }
    }

    /**
     * 下一題
     */
    private fun nextQuestion() {
        if (quizViewModel.nextQuestion()) {
            when {
                quizViewModel.isCheat -> Toast.makeText(this, "作弊就是0分啦", Toast.LENGTH_SHORT).show()
                else -> Toast.makeText(this, "恭喜得到 ${quizViewModel.score} 分", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        questionText.text = quizViewModel.getQuestionText()
    }

    /**
     * 建立啟動器，用來跳轉Activity
     */
    private fun createLauncher() {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                var isCheat =
                    it.data?.getBooleanExtra(CheatActivity.EXTRA_ANSWER_SHOW, false) ?: false
                Log.d(Companion.TAG, "頁面跳轉資料回傳成功")
                Log.d(
                    Companion.TAG,
                    isCheat.toString()
                )
                // 如果有作弊
                if (isCheat) {
                    quizViewModel.isCheat = true
                    Toast.makeText(this, "你作弊", Toast.LENGTH_SHORT).show()
                }
            } else {
                Log.d(Companion.TAG, "頁面跳轉資料回傳失敗")
            }
        }
    }
}