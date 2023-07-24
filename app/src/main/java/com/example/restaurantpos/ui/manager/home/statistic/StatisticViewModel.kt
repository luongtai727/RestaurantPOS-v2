package com.example.restaurantpos.ui.manager.home.statistic

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantpos.db.entity.ItemRevenue
import com.example.restaurantpos.util.Constant
import com.example.restaurantpos.util.DatabaseUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.SortedMap
import java.util.TreeMap

class StatisticViewModel : ViewModel() {
    private val _getTotalRevenue: MutableLiveData<Map<String, Float>> by lazy {
        MutableLiveData<Map<String, Float>>()
    }

    val getTotalRevenue: LiveData<Map<String, Float>> = _getTotalRevenue

    fun getTotalRevenueByTimeAndCategory(time: Int, category: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val listItemRevenue: List<ItemRevenue> = DatabaseUtil.orderDAO.getTotalRevenueByCategory(category)

            // Week- Month- Year: 1- 2- 3
            // Foods- Drinks- Desserts: 1- 2- 3

            _getTotalRevenue.postValue(when (time) {
                Constant.WEEK_TIME -> calculateTotalRevenueByWeek(listItemRevenue)
                Constant.MONTH_TIME -> calculateTotalRevenueByMonth(listItemRevenue)
                else -> calculateTotalRevenueByYear(listItemRevenue)
            })
        }
    }

    fun calculateTotalRevenueByWeek(itemRevenueList: List<ItemRevenue>): SortedMap<String, Float> {
        val revenueByWeek = TreeMap<String, Float>()

        for (itemRevenue in itemRevenueList) {
            val timeFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
            val date = timeFormat.parse(itemRevenue.order_id)
            val calendar = Calendar.getInstance()
            calendar.time = date

            val weekYear = String.format(
                "%04d/%02d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.WEEK_OF_YEAR)
            )

            val totalRevenue = itemRevenue.price * itemRevenue.order_quantity

            if (revenueByWeek.containsKey(weekYear)) {
                revenueByWeek[weekYear] = revenueByWeek[weekYear]!! + totalRevenue
            } else {
                revenueByWeek[weekYear] = totalRevenue
            }
        }

        return revenueByWeek
    }

    private fun calculateTotalRevenueByMonth(itemRevenueList: List<ItemRevenue>): Map<String, Float> {
        val revenueByMonth = mutableMapOf<String, Float>()

        for (itemRevenue in itemRevenueList) {
            val timeFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
            val date = timeFormat.parse(itemRevenue.order_id)
            val calendar = Calendar.getInstance()
            calendar.time = date

            val monthYear = String.format(
                "%04d/%02d",
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1
            )

            val totalRevenue = itemRevenue.price * itemRevenue.order_quantity

            if (revenueByMonth.containsKey(monthYear)) {
                revenueByMonth[monthYear] = revenueByMonth[monthYear]!! + totalRevenue
            } else {
                revenueByMonth[monthYear] = totalRevenue
            }
        }

        return revenueByMonth
    }

    private fun calculateTotalRevenueByYear(itemRevenueList: List<ItemRevenue>): Map<String, Float> {
        val revenueByYear = mutableMapOf<String, Float>()

        for (itemRevenue in itemRevenueList) {
            val timeFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
            val date = timeFormat.parse(itemRevenue.order_id)
            val calendar = Calendar.getInstance()
            calendar.time = date

            val year = String.format("%04d", calendar.get(Calendar.YEAR))

            val totalRevenue = itemRevenue.price * itemRevenue.order_quantity

            if (revenueByYear.containsKey(year)) {
                revenueByYear[year] = revenueByYear[year]!! + totalRevenue
            } else {
                revenueByYear[year] = totalRevenue
            }
        }

        return revenueByYear
    }

}