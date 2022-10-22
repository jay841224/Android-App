package com.geoquiz

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class CheatActivity : AppCompatActivity() {

    companion object {

        /**
         * extra key
         */
        private const val EXTRA_ANSWER_IS_TRUE = "answer_is_true"

        /**
         * extra key
         */
        const val EXTRA_ANSWER_SHOW = "answer_is_shown"

        /**
         * 創建CheatActivity的Intent
         */
        fun newIntent(packageContext: Context, answerIsTrue: Boolean): Intent {
            return  Intent(packageContext, CheatActivity::class.java).apply {
                putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue)
            }
        }
    }

    private lateinit var showAnswerBtn: Button
    private lateinit var answerShownText: TextView

    private var currentAnswer = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cheat)

        showAnswerBtn = findViewById(R.id.cheat_show_btn)
        answerShownText = findViewById(R.id.answer_shown_text)

        setClick()
    }

    /**
     * 設定點擊事件
     */
    private fun setClick() {

        showAnswerBtn.setOnClickListener {

            // 將答案顯示在 textView (從extra拿MainActivity存得值)
            answerShownText.text = intent.getBooleanExtra(Companion.EXTRA_ANSWER_IS_TRUE, false).toString()

            // apply 會回傳 Intent 本身，在 scope 內可以用 this 表示 Intent
            val intentData = Intent().apply {
                this.putExtra(Companion.EXTRA_ANSWER_SHOW, true)
            }
            // 設定 return 結果代碼和回傳值
            setResult(Activity.RESULT_OK, intentData)
        }
    }
}