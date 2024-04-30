package com.example.alertguard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.alertguard.databinding.RowReportListBinding
import com.example.alertguard.db.models.ReportDataModel

class ReportListAdapter(private val reportList: List<ReportDataModel>) :
    RecyclerView.Adapter<ReportListAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = RowReportListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun getItemCount(): Int = reportList.size

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onBind(reportList[position])
    }

    class Holder(private val binding: RowReportListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(reportDataModel: ReportDataModel) {
            binding.emergencyTxt.text = reportDataModel.contact
            binding.latitudeTxt.text = reportDataModel.latitude
            binding.longitude.text = reportDataModel.longitude
        }
    }
}