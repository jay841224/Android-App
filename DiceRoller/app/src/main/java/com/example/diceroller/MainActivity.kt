package com.example.diceroller

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.diceroller.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    /* 讓view一開始就binding，就不用用findViewById來抓view */
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 初始化 binding 物件，您將使用該物件存取 activity_main.xml 版面配置中的 Views
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // init img
        binding.diceImg.setImageResource(R.drawable.dice_1)
        val dice = Dice(6)
        binding.rowButton.setOnClickListener {
            // show toast
            val toast = Toast.makeText(this, "Dice Rolled!", Toast.LENGTH_LONG)
            toast.show()

            val diceNum = dice.roll()

            val curImg = when(diceNum) {
                1 -> R.drawable.dice_1
                2 -> R.drawable.dice_2
                3 -> R.drawable.dice_3
                4 -> R.drawable.dice_4
                5 -> R.drawable.dice_5
                else -> R.drawable.dice_6
            }
            binding.diceImg.setImageResource(curImg)
            /* 講出結果 */
            binding.diceImg.contentDescription = diceNum.toString()
        }
    }

    class Dice(private val numSides: Int) {
        fun roll(): Int {
            return (1..numSides).random()
        }
    }
}