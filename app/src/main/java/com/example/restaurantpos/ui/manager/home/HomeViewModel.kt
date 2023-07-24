package com.example.restaurantpos.ui.manager.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantpos.db.entity.OrderEntity
import com.example.restaurantpos.util.DataUtil
import com.example.restaurantpos.util.DatabaseUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _isDuplicate: MutableLiveData<Float> by lazy {
        MutableLiveData<Float>()
    }

    val isDuplicate: LiveData<Float> = _isDuplicate

    fun getRevenueOfDayOfItem(id_item: Int, time: String) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.getRevenueOfDayOfItem(id_item, time)
        }
    }


/*    fun getRevenueOfDay(time: String) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.getRevenueOfDay(time)
        }
    }*/

/*    fun getRevenueOfDay(nowYear:Int, nowMonth: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            // Lấy số ngày ra
            val countDay = DataUtil.getNumberOfDayInMonth(nowYear, nowMonth)
            for (i in 1..countDay) {
                val amount = DatabaseUtil.getRevenueOfDay("$nowYear/$nowMonth/$i")

                // Gửi lên UI,đưa vào biểu đồ
                _isDuplicate.postValue(amount)
            }
            _isDuplicate.postValue(-1f)
        }
    }*/





    // Qnew
    private val _revenue : MutableLiveData<MutableList<Float>> by lazy {
        MutableLiveData<MutableList<Float>>()
    }

    val revenue: LiveData<MutableList<Float>> = _revenue

/*    fun getAllOrder(){
        CoroutineScope(Dispatchers.IO).launch {
            val carts: List<OrderEntity> = DatabaseUtil.getAllOrder()
            val b = carts
        }
    }*/


/*    fun getRevenueOfDay(nowYear:Int, nowMonth: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val countDay = DataUtil.getNumberOfDayInMonth(nowYear, nowMonth)
            // Đây là cái màn mình cần đưa vào Trục tung (Cho từng cột)
            val listRevenue = ArrayList<Float>()

            for (i in 1..countDay) {
                val amount = DatabaseUtil.getAllOrder("$nowYear/0$nowMonth/$i")
                listRevenue.add(amount)
            }
            // postValue update data (LiveData<MutableList<Float>>) để cập nhật dữ liệu
            _revenue.postValue(listRevenue)
        }
    }*/

    fun getRevenueOfDay(nowYear:Int, nowMonth: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val countDay = DataUtil.getNumberOfDayInMonth(nowYear, nowMonth)
            val listRevenue = ArrayList<Float>()
            val monthString = if (nowMonth < 10) "0$nowMonth" else "$nowMonth"

            for (i in 1..countDay) {
                val dayString = if (i < 10) "0$i" else "$i"

                val amount = DatabaseUtil.getAllOrder("$nowYear/$monthString/$dayString")
                listRevenue.add(amount)
            }

            _revenue.postValue(listRevenue)
        }
    }



}