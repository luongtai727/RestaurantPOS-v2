package com.example.restaurantpos.ui.manager.customer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.restaurantpos.db.entity.CustomerEntity
import com.example.restaurantpos.util.DatabaseUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CustomerViewModel : ViewModel() {

    private val _getIDWhenInsertOrderSuccess: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>()
    }

    val getIDWhenInsertOrderSuccess: LiveData<Long> = _getIDWhenInsertOrderSuccess

    private val _getListCustomerObserver: MutableLiveData<MutableList<CustomerEntity>> by lazy {
        MutableLiveData<MutableList<CustomerEntity>>()
    }

    val getListCustomerObserver: LiveData<MutableList<CustomerEntity>>  = _getListCustomerObserver


    fun getListCustomerByPhoneForSearch(phone: String) =
        DatabaseUtil.getListCustomerByPhoneForSearch(phone)

    fun getListCustomerByPhoneForAdd(phone: String) =
        DatabaseUtil.getListCustomerByPhoneForAdd(phone)

    fun getListCustomer() = DatabaseUtil.getListCustomer()


    /*    fun addCustomer(data: CustomerEntity) {
            CoroutineScope(Dispatchers.IO).launch{
                DatabaseUtil.addCustomer(data)
            }
        }*/

    fun searchCustomerByKey(key: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _getListCustomerObserver.postValue(DatabaseUtil.getListCustomerByPhoneForSearch(key))
        }
    }

    // Lắng nghe: Khi add Customer mới thì nó sẽ trả cho mình thêm 1 cái IdCustomer
    // Mang IdCustomer đi xử lý ở bảng Order.customer_id
    fun addCustomer(customerRecord: CustomerEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            val customerId: Long = DatabaseUtil.addCustomer(customerRecord)
            _getIDWhenInsertOrderSuccess.postValue(customerId)
        }
    }

    // Qnew
    fun updateCustomerTotalAndRank(customerId: Int, totalPayment: Double, customerRankId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            DatabaseUtil.customerDAO.updateCustomerTotalAndRank(
                customerId,
                totalPayment,
                customerRankId
            )
        }
    }
}