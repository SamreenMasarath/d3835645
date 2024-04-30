package com.example.alertguard.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alertguard.Singleton
import com.example.alertguard.adapter.ReportListAdapter
import com.example.alertguard.databinding.FragmentHistoryBinding
import com.example.alertguard.db.AppDatabase
import com.example.alertguard.db.models.ReportDataModel

class HistoryFragment: Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private var reportList : List<ReportDataModel>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getData()
    }

    private fun getData() {
        val thread = Thread {
            reportList = AppDatabase.getDatabase().reportDataDao().getReportData(Singleton.userModel?.email!!)
        }
        thread.start()
        thread.join()

        binding.reportRV.layoutManager = LinearLayoutManager(requireContext())
        binding.reportRV.adapter = reportList?.let { ReportListAdapter(it) }
    }

}