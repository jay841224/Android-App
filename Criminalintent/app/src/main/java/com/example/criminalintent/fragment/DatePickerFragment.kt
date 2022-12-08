package com.example.criminalintent.fragment

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class DatePickerFragment : DialogFragment() {

    companion object {

        private const val ARG_DATE = "date"

        fun newInstance(date: Date): DatePickerFragment {

            return DatePickerFragment().apply {
                arguments = bundleOf(ARG_DATE to date)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dateListener =
            DatePickerDialog.OnDateSetListener { _: DatePicker, year: Int, month: Int, day: Int ->
                val resultDate: Date = GregorianCalendar(year, month, day).time

                this@DatePickerFragment.parentFragmentManager?.setFragmentResult(
                    CrimeFragment.DATE_KEY,
                    bundleOf(CrimeFragment.DATE_RESULT to resultDate)
                )

            }
        val date = arguments?.getSerializable(ARG_DATE) as Date
        val calendar = Calendar.getInstance()
        calendar.time = date
        var initialYear = calendar.get(Calendar.YEAR)
        var initialMonth = calendar.get(Calendar.MONTH)
        var initialDay = calendar.get(Calendar.DATE)

        return DatePickerDialog(
            requireContext(),
            dateListener,
            initialYear,
            initialMonth,
            initialDay
        )
    }
}