package com.malec.reportlist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import com.malec.presentation.base.BaseFragment
import com.malec.reportlist.databinding.FragmentReportListBinding

class ReportListFragment : BaseFragment<FragmentReportListBinding>() {
    override fun createViewBinding(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        attachToParent: Boolean
    ) = FragmentReportListBinding.inflate(inflater)

    companion object {
        fun newInstance() = ReportListFragment()
    }
}