package com.example.criminalintent.fragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.criminalintent.R
import com.example.criminalintent.data.Crime
import com.example.criminalintent.viewModel.CrimeDetailViewModel
import com.example.criminalintent.viewModel.CrimeListViewModel
import java.util.Date
import java.util.UUID

class CrimeFragment : Fragment() {

    companion object {

        private const val ARG_CRIME_ID = "crime_id"
        private const val DATE_DIALOG = "date_dialog"
        const val DATE_KEY = "date_key"
        const val DATE_RESULT = "date_result"

        fun newInstance(crimeId: UUID): CrimeFragment {
            val args = Bundle().apply {
                putSerializable(ARG_CRIME_ID, crimeId)
            }

            // 將要傳遞的變數放到 arguments 中
            return CrimeFragment().apply {
                arguments = args
            }
        }
    }

    private lateinit var crimeData: Crime

    private lateinit var crimeTitle: EditText
    private lateinit var dateButton: Button
    private lateinit var solvedCheckBox: CheckBox

    /**
     * 綁定 ViewModel
     */
    private val crimeDetailViewModel: CrimeDetailViewModel by lazy {
        ViewModelProvider(this)[CrimeDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var crimeId: UUID = arguments?.getSerializable(ARG_CRIME_ID) as UUID
        crimeDetailViewModel.loadCrime(crimeId)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment 在 onCreateView 中建立view並綁定物件
        val view = inflater.inflate(R.layout.fragment_crime, container, false)

        crimeTitle = view.findViewById(R.id.crime_title) as EditText
        dateButton = view.findViewById(R.id.crime_date) as Button
        solvedCheckBox = view.findViewById(R.id.crime_solved) as CheckBox
        crimeData = Crime()

        dateButton.apply {
            this.text = crimeData.date.toString()
            // 這邊暫時不讓用戶點擊
//            this.isEnabled = false
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crimeDetailViewModel.crimeLiveData.observe(
            viewLifecycleOwner,
            Observer { crime ->
                crime?.let {
                    this.crimeData = crime
                    updateUI()
                }
            }
        )
    }

    override fun onStart() {
        super.onStart()

        // 這個 watcher 可以設定到 EditText 上
        // 將 watcher 放在 onStart 可以避免在像是轉螢幕時去觸發到 beforeTextChanged 這類的方法
        val titleWatcher = createTitleWatcher()

        // 設定 watcher
        crimeTitle.addTextChangedListener(titleWatcher)

        // also & apply 會將自己回傳
        solvedCheckBox.apply {
            // 這邊用 lambda 寫法取代下面那個方式
            this.setOnCheckedChangeListener { _, isChecked ->
                crimeData.isSolved = isChecked
            }
        }

        dateButton.setOnClickListener {
            this@CrimeFragment.parentFragmentManager.setFragmentResultListener(
                DATE_KEY,
                this@CrimeFragment
            ) { _, bundle ->
                val result = bundle.getSerializable(DATE_RESULT) as Date
                crimeData.date = result

                updateUI()
            }
            // @ -> 是指定的意思
            // parentFragmentManager 是用 parent 的 fragmentManager
            DatePickerFragment.newInstance(crimeData.date).apply {
                show(this@CrimeFragment.parentFragmentManager, DATE_DIALOG)
            }
        }

        /*
        沒有用 lambda 的寫法
        val ttt = object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton?, isChecked: Boolean) {
                crimeData.isSolved = isChecked
            }
        */
    }

    override fun onStop() {
        super.onStop()
        crimeDetailViewModel.saveCrime(crimeData)
    }

    /**
     * 生成 title watcher
     */
    private fun createTitleWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            // CharSequence 是輸入的內容
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                crimeData.title = s.toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
    }

    private fun updateUI() {
        crimeTitle.setText(crimeData.title)
        dateButton.text = crimeData.date.toString()
        solvedCheckBox.isChecked = crimeData.isSolved
    }
}
