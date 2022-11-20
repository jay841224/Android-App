package com.example.criminalintent.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.criminalintent.viewModel.CrimeListViewModel
import com.example.criminalintent.R
import com.example.criminalintent.data.Crime
import java.text.DateFormat
import java.util.Locale
import java.util.UUID

class CrimeListFragment : Fragment() {

    /**
     * callBack 介面
     */
    interface CallBacks {
        fun onCrimesSelected(crimeId: UUID)
    }

    companion object {
        private const val TAG = "CrimeListFragment"

        fun newInstance(): CrimeListFragment {
            return CrimeListFragment()
        }
    }

    private lateinit var crimeRecyclerView: RecyclerView
    private var adapter: CrimeAdapter? = null

    private var callBacks: CallBacks? = null

    /**
     * 綁定 ViewModel
     */
    private val crimeListViewModel: CrimeListViewModel by lazy {
        ViewModelProvider(this)[CrimeListViewModel::class.java]
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Log.d(Companion.TAG, "Total crime: ${crimeListViewModel.crimes.size}")
//    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // 這邊做了一個類型轉換，也就代表這個託管 Activity(context) 需要去實作 CallBacks 這個介面了
        callBacks = context as CallBacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_crime_list, container, false)

        crimeRecyclerView = view.findViewById(R.id.crime_recycler_view) as RecyclerView
        // 決定 RecyclerView 樣式
        crimeRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // 響應式
        crimeListViewModel.crimeListLiveData.observe(
            viewLifecycleOwner,
            Observer { crimes ->
            crimes.let {
                Log.i(TAG, "Get crimes ${crimes.size}")
                updateUI(crimes)
            }
        })

        // 先嘗試塞入一筆資料
//        var testCrime = Crime()
//        testCrime.title = "Crime 1"
//        testCrime.isSolved = true
//        crimeListViewModel.crimeRepository.clearAll()
//        crimeListViewModel.crimeRepository.insertCrime(testCrime)
    }

    override fun onDetach() {
        super.onDetach()
        callBacks = null
    }

    /**
     * RecyclerView 只會創建 ViewHolder
     * ViewHolder 裡面去包含 itemView
     */
    private inner class CrimeHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var crime: Crime

        private val titleTextView: TextView = itemView.findViewById(R.id.crime_title)
        private val dateTextView: TextView = itemView.findViewById(R.id.crime_date)
        private val solvedImgView: ImageView = itemView.findViewById(R.id.crime_solved)

        init {
            // CrimeHolder 直接繼承了 OnClickListener, 所以這邊就直接設this為一個click事件（有覆寫了 onClick fun）
            itemView.setOnClickListener(this)
        }

        // 綁綁訂的工作放在 Holder 內處理
        fun bind(crime: Crime) {
            this.crime = crime
            titleTextView.text = crime.title
            dateTextView.text = DateFormat.getDateInstance(DateFormat.FULL, Locale.TAIWAN).format(crime.date).toString()
            solvedImgView.visibility = if (crime.isSolved) View.VISIBLE else View.GONE
        }

        override fun onClick(v: View?) {
//            Toast.makeText(context, "${crime.title} pressed!", Toast.LENGTH_SHORT).show()
            callBacks?.onCrimesSelected(crime.id)
        }
    }

    /**
     * 1. adapter 用來創建必要的 ViewHolder
     * 2. 綁定 ViewHolder 至數據層
     */
    // class 建構子這邊用 var or val 讓建構子外也可以使用該變數
    private inner class CrimeAdapter(var crimes: List<Crime>) : RecyclerView.Adapter<CrimeHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrimeHolder {
            val view = layoutInflater.inflate(R.layout.list_item_crime, parent, false)
            return CrimeHolder(view)
        }

        override fun onBindViewHolder(holder: CrimeHolder, position: Int) {
            val crime = crimes[position]
            holder.bind(crime)
        }

        override fun getItemCount(): Int {
            return crimes.size
        }
    }

    /**
     * 綁定 adapter 到 recyclerView 上面，讓 adapter 去控制
     */
    private fun updateUI(crimes: List<Crime>) {
//        val crimes = crimeListViewModel.crimes
        adapter = CrimeAdapter(crimes)
        crimeRecyclerView.adapter = adapter
    }
}