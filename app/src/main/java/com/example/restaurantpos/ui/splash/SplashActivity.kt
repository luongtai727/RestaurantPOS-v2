package com.example.restaurantpos.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import androidx.lifecycle.ViewModelProvider
import com.example.restaurantpos.base.BaseActivity
import com.example.restaurantpos.databinding.ActivitySplashBinding
import com.example.restaurantpos.db.entity.AccountEntity
import com.example.restaurantpos.db.entity.AccountRoleEntity
import com.example.restaurantpos.db.entity.AccountStatusEntity
import com.example.restaurantpos.db.entity.CartItemEntity
import com.example.restaurantpos.db.entity.CartItemStatusEntity
import com.example.restaurantpos.db.entity.CategoryEntity
import com.example.restaurantpos.db.entity.CouponEntity
import com.example.restaurantpos.db.entity.CustomerEntity
import com.example.restaurantpos.db.entity.CustomerRankEntity
import com.example.restaurantpos.db.entity.ItemEntity
import com.example.restaurantpos.db.entity.OrderEntity
import com.example.restaurantpos.db.entity.OrderStatusEntity
import com.example.restaurantpos.db.entity.ShiftEntity
import com.example.restaurantpos.db.entity.TableEntity
import com.example.restaurantpos.db.entity.TableStatusEntity
import com.example.restaurantpos.ui.login.LoginActivity
import com.example.restaurantpos.util.Constant
import com.example.restaurantpos.util.DataUtil


@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>() {

    lateinit var viewModel: SplashViewModel
    private val DELAY_TIME = 1500L


    override fun initOnCreate() {
        viewModel = ViewModelProvider(this).get(SplashViewModel::class.java)
        /** Set Data for DB */
        createDataFirst()
        /** Start Login Screen */
        startLoginActivity()
    }

    private fun createDataFirst() {
        /** 1. Account */
        viewModel.addListAccount(
            listOf(
                AccountEntity(
                    1,
                    "Admin",
                    "1995/03/02",
                    "08010802100",
                    "admin",
                    DataUtil.convertToMD5("123"+ Constant.SECURITY_SALT),
                    0,
                    true
                ),
                AccountEntity(
                    2,
                    "Anh Thu",
                    "1995/03/02",
                    "080101232100",
                    "anhthu",
                    DataUtil.convertToMD5("123"+ Constant.SECURITY_SALT),
                    1,
                    true
                ),
                AccountEntity(
                    3,
                    "Anh Manh",
                    "1995/03/02",
                    "08010531100",
                    "anhmanh",
                    DataUtil.convertToMD5("123"+ Constant.SECURITY_SALT),
                    2,
                    true
                ),
            )
        )
        /** 1.1. Account_Role */
        viewModel.addListAccountRole(
            listOf(
                AccountRoleEntity(0, "Manager"),
                AccountRoleEntity(1, "Receptionist"),
                AccountRoleEntity(2, "Kitchen")
            )
        )
        /** 1.2. Account_Status */
        viewModel.addListAccountStatus(
            listOf(
                AccountStatusEntity(false, "Inactive"),
                AccountStatusEntity(true, "Active")
            )
        )

        /** 2. Table */
        viewModel.addTable(this, TableEntity(1, "Table 01", 4, 0))
        viewModel.addTable(this, TableEntity(2, "Table 02", 4, 0))
        viewModel.addTable(this, TableEntity(3, "Table 03", 4, 0))
        viewModel.addTable(this, TableEntity(4, "Table 04", 4, 0))
        viewModel.addTable(this, TableEntity(5, "Table 05", 4, 0))
        viewModel.addTable(this, TableEntity(6, "Table 06", 4, 0))
        viewModel.addTable(this, TableEntity(7, "Table 07", 4, 0))
        viewModel.addTable(this, TableEntity(8, "Table 08", 4, 0))
        viewModel.addTable(this, TableEntity(9, "Table 09", 4, 0))
        viewModel.addTable(this, TableEntity(10, "Table 10", 4, 0))
        viewModel.addTable(this, TableEntity(11, "Table 11", 4, 0))

        /** 2.1. Table_Status */
        viewModel.addListTableStatus(
            listOf(
                TableStatusEntity(0, "Empty"),
                TableStatusEntity(1, "In New Order"),
                TableStatusEntity(2, "In Old Order"),
                TableStatusEntity(3, "Problem")
            )
        )

        /** 3. Category */
        viewModel.addCategory(CategoryEntity(1, "FOODS"))
        viewModel.addCategory(CategoryEntity(2, "DRINKS"))
        viewModel.addCategory(CategoryEntity(3, "DESSERTS"))

        /** 4. Item */
/*        viewModel.addCategoryItem(ItemEntity(1, "Mon Nhau 1", 131.1f, "", 5, 1))
        viewModel.addCategoryItem(ItemEntity(2, "Mon Nhau 2", 1111.1f, "", 1, 1))
        viewModel.addCategoryItem(ItemEntity(3, "Mon Nhau 3", 22.1f, "", 3, 2))
        viewModel.addCategoryItem(ItemEntity(4, "Mon Nhau 5", 13.1f, "", 5, 2))
        viewModel.addCategoryItem(ItemEntity(5, "Mon Nhau 6", 15.1f, "", 1, 3))
        viewModel.addCategoryItem(ItemEntity(6, "Mon Nhau 7", 111.1f, "", 3, 3))*/

        /** 4.1. Cart_Item_Status */
        viewModel.addListCartItemStatus(
            listOf(
                CartItemStatusEntity(0, "Waiting"),
                CartItemStatusEntity(1, "In Progress"),
                CartItemStatusEntity(2, "Done"),
                CartItemStatusEntity(3, "Served")
            )
        )

        /** 4.2. Order_Status */
        viewModel.addListOrderStatus(
            listOf(
                OrderStatusEntity(0, "After clicking the table"),
                OrderStatusEntity(1, "After Ordering"),
                OrderStatusEntity(2, "After Checkout")
            )
        )

        /** 5. Customer  */
        viewModel.addCustomer(CustomerEntity(1, "Quang", "08034931491", "1995/03/02", 0.0, 0))
        viewModel.addCustomer(CustomerEntity(2, "Chuong", "09034931492", "1995/03/03", 3000.0, 1))
        viewModel.addCustomer(CustomerEntity(3, "Anh Bang", "07034931493", "1995/03/04", 11000.0, 2))
        viewModel.addCustomer(CustomerEntity(4, "Chi Trang", "07034931422", "1995/03/04", 22000.0, 3))

        /** 6. Shift  */

        viewModel.addListShift(
            listOf(
                ShiftEntity("1", "Morning", "8:00", "12:00"),
                ShiftEntity("2", "Afternoon", "12:00", "18:00"),
                ShiftEntity("3", "Night", "18:00", "22:00")
            )
        )


        /** 7. Coupon  */

        viewModel.addCoupon(CouponEntity(1, "2023/07/10", "QUANG1", 10, 1))
        viewModel.addCoupon(CouponEntity(2, "2023/07/11", "QUANG2", 21, 0))
        viewModel.addCoupon(CouponEntity(3, "2023/07/12", "QUANG3", 32, 1))
        viewModel.addCoupon(CouponEntity(4, "2023/07/13", "QUANG4", 43, 0))
        viewModel.addCoupon(CouponEntity(5, "2023/07/14", "QUANG5", 54, 1))

        /** 8. Customer rank  */
        viewModel.addCustomerRank(CustomerRankEntity(1, "rank 0"))
        viewModel.addCustomerRank(CustomerRankEntity(2, "rank 1"))
        viewModel.addCustomerRank(CustomerRankEntity(3, "rank 2"))
        viewModel.addCustomerRank(CustomerRankEntity(4, "rank 3"))

        /** 9. order   */
       /* OrderEntity(order_id="2023/07/22  16:20:04", customer_id=1, table_id=2, created_by_account_id=2, order_create_time="2023/07/22  16:20:04", paid_time="2023/07/22  16:20:40", bill_total=302.83002f, order_status_id=2, coupon=0, customer_rank=0, sub_total=275.30002f, cash=8000, change="7697,2")
        viewModel.addOrder(OrderEntity(order_id="2023/07/01  16:20:04", customer_id=1, table_id=2, created_by_account_id=2, order_create_time="2023/07/01  16:20:04", paid_time="2023/07/22  16:20:40", bill_total=100.83002f, order_status_id=2, coupon=0, customer_rank=0, sub_total=75.30002f, cash=8000, change="7697,2"))
        viewModel.addOrder(OrderEntity(order_id="2023/07/02  16:20:04", customer_id=1, table_id=2, created_by_account_id=2, order_create_time="2023/07/02  16:20:04", paid_time="2023/07/22  16:20:40", bill_total=105.83002f, order_status_id=2, coupon=0, customer_rank=0, sub_total=85.30002f, cash=8000, change="7697,2"))
        viewModel.addOrder(OrderEntity(order_id="2023/07/03  16:20:04", customer_id=1, table_id=2, created_by_account_id=2, order_create_time="2023/07/03  16:20:04", paid_time="2023/07/22  16:20:40", bill_total=200.83002f, order_status_id=2, coupon=0, customer_rank=0, sub_total=195.30002f, cash=8000, change="7697,2"))
        viewModel.addOrder(OrderEntity(order_id="2023/07/05  16:20:04", customer_id=1, table_id=2, created_by_account_id=2, order_create_time="2023/07/04  16:20:04", paid_time="2023/07/22  16:20:40", bill_total=250.83002f, order_status_id=2, coupon=0, customer_rank=0, sub_total=235.30002f, cash=8000, change="7697,2"))
        viewModel.addOrder(OrderEntity(order_id="2023/07/06  16:20:04", customer_id=1, table_id=2, created_by_account_id=2, order_create_time="2023/07/05  16:20:04", paid_time="2023/07/22  16:20:40", bill_total=280.83002f, order_status_id=2, coupon=0, customer_rank=0, sub_total=275.30002f, cash=8000, change="7697,2"))
        viewModel.addOrder(OrderEntity(order_id="2023/07/08  16:20:04", customer_id=1, table_id=2, created_by_account_id=2, order_create_time="2023/07/06  16:20:04", paid_time="2023/07/22  16:20:40", bill_total=290.83002f, order_status_id=2, coupon=0, customer_rank=0, sub_total=285.30002f, cash=8000, change="7697,2"))

        val list = ArrayList<CartItemEntity>()
        list.add(CartItemEntity(cart_item_id=1, item_id=5, order_id="2023/07/01  16:36:41", order_quantity=2, note= "", cart_order_time="16:36:42", cart_item_status_id=2))
        list.add(CartItemEntity(cart_item_id=2, item_id=5, order_id="2023/07/02  16:36:41", order_quantity=2, note= "", cart_order_time="16:36:42", cart_item_status_id=2))
        list.add(CartItemEntity(cart_item_id=3, item_id=5, order_id="2023/07/03  16:36:41", order_quantity=2, note= "", cart_order_time="16:36:42", cart_item_status_id=2))
        list.add(CartItemEntity(cart_item_id=4, item_id=1, order_id="2023/07/04  16:36:41", order_quantity=2, note= "", cart_order_time="16:36:42", cart_item_status_id=2))
        list.add(CartItemEntity(cart_item_id=5, item_id=1, order_id="2023/07/05  16:36:41", order_quantity=2, note= "", cart_order_time="16:36:42", cart_item_status_id=2))
        list.add(CartItemEntity(cart_item_id=6, item_id=8, order_id="2023/07/06  16:36:41", order_quantity=2, note= "", cart_order_time="16:36:42", cart_item_status_id=2))
        list.add(CartItemEntity(cart_item_id=7, item_id=8, order_id="2023/07/07  16:36:41", order_quantity=2, note= "", cart_order_time="16:36:42", cart_item_status_id=2))
        list.add(CartItemEntity(cart_item_id=8, item_id=8, order_id="2023/07/08  16:36:41", order_quantity=2, note= "", cart_order_time="16:36:42", cart_item_status_id=2))
        list.add(CartItemEntity(cart_item_id=9, item_id=1, order_id="2023/07/09  16:36:41", order_quantity=2, note= "", cart_order_time="16:36:42", cart_item_status_id=2))
        list.add(CartItemEntity(cart_item_id=10, item_id=1, order_id="2023/07/10  16:36:41", order_quantity=2, note= "", cart_order_time="16:36:42", cart_item_status_id=2))

        list.add(CartItemEntity(cart_item_id=11, item_id=1, order_id="2023/06/10  16:36:41", order_quantity=2, note= "", cart_order_time="16:36:42", cart_item_status_id=2))
        list.add(CartItemEntity(cart_item_id=12, item_id=1, order_id="2023/05/10  16:36:41", order_quantity=2, note= "", cart_order_time="16:36:42", cart_item_status_id=2))
        list.add(CartItemEntity(cart_item_id=13, item_id=1, order_id="2023/04/10  16:36:41", order_quantity=2, note= "", cart_order_time="16:36:42", cart_item_status_id=2))
        list.add(CartItemEntity(cart_item_id=14, item_id=1, order_id="2023/03/10  16:36:41", order_quantity=2, note= "", cart_order_time="16:36:42", cart_item_status_id=2))
        list.add(CartItemEntity(cart_item_id=15, item_id=1, order_id="2023/02/10  16:36:41", order_quantity=2, note= "", cart_order_time="16:36:42", cart_item_status_id=2))
        list.add(CartItemEntity(cart_item_id=16, item_id=1, order_id="2023/01/10  16:36:41", order_quantity=2, note= "", cart_order_time="16:36:42", cart_item_status_id=2))

        list.add(CartItemEntity(cart_item_id=17, item_id=5, order_id="2023/06/10  16:36:41", order_quantity=2, note= "", cart_order_time="16:36:42", cart_item_status_id=2))
        list.add(CartItemEntity(cart_item_id=18, item_id=5, order_id="2023/05/10  16:36:41", order_quantity=2, note= "", cart_order_time="16:36:42", cart_item_status_id=2))
        list.add(CartItemEntity(cart_item_id=19, item_id=5, order_id="2023/04/10  16:36:41", order_quantity=2, note= "", cart_order_time="16:36:42", cart_item_status_id=2))
        list.add(CartItemEntity(cart_item_id=20, item_id=8, order_id="2023/03/10  16:36:41", order_quantity=2, note= "", cart_order_time="16:36:42", cart_item_status_id=2))
        list.add(CartItemEntity(cart_item_id=21, item_id=8, order_id="2023/02/10  16:36:41", order_quantity=2, note= "", cart_order_time="16:36:42", cart_item_status_id=2))
        list.add(CartItemEntity(cart_item_id=22, item_id=8, order_id="2023/01/10  16:36:41", order_quantity=2, note= "", cart_order_time="16:36:42", cart_item_status_id=2))

        viewModel.addListCartItem(list)*/

    }

    private fun startLoginActivity() {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
            finish()
        }, DELAY_TIME)
    }

    override fun getInflaterViewBinding(layoutInflater: LayoutInflater): ActivitySplashBinding {
        return ActivitySplashBinding.inflate(layoutInflater)
    }
}