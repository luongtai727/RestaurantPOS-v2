package com.example.restaurantpos.ui.manager.home.statistic

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restaurantpos.databinding.FragmentStatisticBinding
import com.example.restaurantpos.util.Constant
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet

/*
Ở màn home chỉ hiển thị đơn thuần doanh thu của ngày hôm nay thôi
Title:
 - Ở Home là ngày. Bỏ qua ngày ở Fragment
 - Ở Fragment --> Truyền vào tuần/tháng/năm sẽ có tuần/tháng/năm
*/
class StatisticFragment : Fragment() {
    lateinit var binding: FragmentStatisticBinding
    lateinit var adapterStatistic: StatisticAdapter

    // Qnew
    private lateinit var statisticModel: StatisticViewModel
    var time = 1
    var category = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statisticModel = ViewModelProvider(this).get(StatisticViewModel::class.java)

        /** Device's Back Button*/
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        /** imgBack */
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }

        /** Adapter */
/*        val listData = ArrayList<ModelStatistic>()
        listData.add(ModelStatistic("January", 1.0f))
        listData.add(ModelStatistic("February", 2.0f))
        listData.add(ModelStatistic("March", 3.0f))
        listData.add(ModelStatistic("April", 4.0f))
        listData.add(ModelStatistic("May", 5.0f))
        listData.add(ModelStatistic("June", 6.0f))
        listData.add(ModelStatistic("July", 7.0f))
        listData.add(ModelStatistic("August", 8.0f))
        listData.add(ModelStatistic("September", 9.0f))
        listData.add(ModelStatistic("October", 10.0f))
        listData.add(ModelStatistic("November", 11.0f))
        listData.add(ModelStatistic("December", 12.0f))

        adapterStatistic = StatisticAdapter(requireContext(), ArrayList<ModelStatistic>(), 15f)
        binding.rcyStatistic.adapter = adapterStatistic
        adapterStatistic.setListData(listData)*/


        statisticModel.getTotalRevenueByTimeAndCategory(time, category)

        initEvents()

        initObserver()

    }

    private fun initObserver() {
        statisticModel.getTotalRevenue.observe(viewLifecycleOwner){
            create_graph(binding.chart, it.keys.toList(), it.values.toList())
        }
    }

    private fun initEvents() {
        categoryChangeListener()
        timeChangeListener()
    }

    private fun timeChangeListener() {
        binding.rdgTime.setOnCheckedChangeListener( RadioGroup.OnCheckedChangeListener { _, i ->
            // This will get the radiobutton that has changed in its check state
            val checkedRadioButton =  binding.rdgTime.findViewById<RadioButton>(i)
            // This puts the value (true/false) into the variable
            val isChecked = checkedRadioButton.isChecked

            // If the radiobutton that has changed in check state is now checked
            if (isChecked) {
                time = when (checkedRadioButton.text) {
                    "Week" -> Constant.WEEK_TIME  // 1
                    "Month" -> Constant.MONTH_TIME // 2
                    else -> Constant.YEAR_TIME  // 3
                }
            }

            statisticModel.getTotalRevenueByTimeAndCategory(time, category)
        })
    }

    private fun categoryChangeListener() {
        binding.rdgCategory.setOnCheckedChangeListener( RadioGroup.OnCheckedChangeListener { _, i ->

            val checkedRadioButton =  binding.rdgCategory.findViewById<RadioButton>(i)
            val isChecked = checkedRadioButton.isChecked

            if (isChecked) {
                category = when (checkedRadioButton.text) {
                    "Foods" -> Constant.FOODS_CATEGORY  // 1
                    "Drinks" -> Constant.DRINKS_CATEGORY  // 2
                    else -> Constant.DESSERTS_CATEGORY  // 3
                }
            }


            statisticModel.getTotalRevenueByTimeAndCategory(time, category)
        })
    }


    private fun create_graph(chart: BarChart, graph_label: List<String?>?, userScore: List<Float>) {
        try {
            chart.setDrawBarShadow(false)
            chart.setDrawValueAboveBar(true)
            chart.description.isEnabled = false
            chart.setPinchZoom(false)
            chart.setDrawGridBackground(false)
            val yAxis: YAxis = chart.axisLeft
            yAxis.valueFormatter =
                IAxisValueFormatter { value, axis -> value.toInt().toString() }
            yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART)
            yAxis.granularity = 1f
            yAxis.isGranularityEnabled = true
            chart.axisRight.isEnabled = false
            val xAxis: XAxis = chart.xAxis
            xAxis.granularity = 1f
            xAxis.isGranularityEnabled = true
            xAxis.setCenterAxisLabels(true)
            xAxis.setDrawGridLines(true)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.valueFormatter = IndexAxisValueFormatter(graph_label)
            val yVals1: MutableList<BarEntry> = ArrayList()
            for (i in userScore.indices) {
                yVals1.add(BarEntry(i * 1.0f + 0.5f, userScore[i]))
            }
            val set1: BarDataSet
            if (chart.data != null && chart.data.dataSetCount > 0) {
                set1 = chart.data.getDataSetByIndex(0) as BarDataSet
                set1.values = yVals1
                chart.data.notifyDataChanged()
                chart.notifyDataSetChanged()
            } else {
                set1 = BarDataSet(yVals1, "")
                set1.color = Color.rgb(242, 15, 15)
                val dataSets = ArrayList<IBarDataSet>()
                dataSets.add(set1)
                val data = BarData(dataSets)
                chart.data = data
            }
            chart.setFitBars(true)
            val l: Legend = chart.legend
            l.formSize = 12f
            l.form = Legend.LegendForm.SQUARE
            l.position = Legend.LegendPosition.RIGHT_OF_CHART_INSIDE
            l.textSize = 10f
            l.textColor = Color.BLACK
            l.xEntrySpace = 5f
            l.yEntrySpace = 5f
            chart.invalidate()
            chart.animateY(2000)
            binding.chart.setVisibleXRangeMaximum(5f)
            binding.chart.moveViewToX(0.5f)

        } catch (ignored: Exception) {

        }
    }
}