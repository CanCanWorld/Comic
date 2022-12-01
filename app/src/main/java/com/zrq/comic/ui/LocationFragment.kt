package com.zrq.comic.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.zrq.comic.databinding.FragmentLocationBinding
import com.zrq.comic.util.CellInfo
import com.zrq.comic.util.GpsInfo

class LocationFragment : BaseFragment<FragmentLocationBinding>() {
    override fun providedViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentLocationBinding {
        return FragmentLocationBinding.inflate(layoutInflater)
    }

    override fun initData() {

        CellInfo.init(requireContext())
        GpsInfo.init(requireContext())
    }

    @SuppressLint("SetTextI18n")
    override fun initEvent() {

        mBinding.btnGps.setOnClickListener {
            val gpsInfo: Array<String> = GpsInfo.getGpsInfo()
            val info = "经度: " + gpsInfo[GpsInfo.LONGITUDE] + "纬度: " + gpsInfo[GpsInfo.LATITUDE]
            mBinding.tvInfo.text = "$info\n${mBinding.tvInfo.text}"
        }

        mBinding.btnCell.setOnClickListener {
            val cellInfo: Array<String> = CellInfo.getCellInfo()
            val info = "cell_id: " + cellInfo[CellInfo.CELL_ID] + "lac: " + cellInfo[CellInfo.LAC] + "mnc: " + cellInfo[CellInfo.MNC]
            mBinding.tvInfo.text = "$info\n${mBinding.tvInfo.text}"
        }

    }
}
