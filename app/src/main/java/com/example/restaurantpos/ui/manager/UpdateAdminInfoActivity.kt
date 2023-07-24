package com.example.restaurantpos.ui.manager

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import com.example.restaurantpos.databinding.ActivityUpdateAccountInfoBinding
import com.example.restaurantpos.db.entity.AccountEntity
import com.example.restaurantpos.ui.main.MainManagerActivity
import com.example.restaurantpos.ui.manager.user.UserViewModel
import com.example.restaurantpos.util.Constant
import com.example.restaurantpos.util.DataUtil
import com.example.restaurantpos.util.SharedPreferencesUtils
import com.example.restaurantpos.util.openActivity
import com.example.restaurantpos.util.showToast
import java.util.Calendar

class UpdateAdminInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityUpdateAccountInfoBinding
    private lateinit var viewModel: UserViewModel

    val calendar = Calendar.getInstance()
    val startYear = calendar.get(Calendar.YEAR) - 20
    val startMonth = calendar.get(Calendar.MONTH) - 5
    val startDay = calendar.get(Calendar.DAY_OF_MONTH) - 10
    lateinit var  accountEntity: AccountEntity
    var name: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateAccountInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        /** Khai báo viewModel --> Dùng phương thức addUser --> set ADD Button */
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)


        /** Device's Back Button*/
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                backToManager()
            }
        }
        this.onBackPressedDispatcher.addCallback(this, callback)

        /** Cancel/Back */
        binding.txtCancel.setOnClickListener {
            backToManager()
        }
        binding.imgBack.setOnClickListener {
            backToManager()
        }

            binding.imgDate.setOnClickListener {
                DatePickerDialog(
                    this,
                    DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                        val m = if (month < 10){
                            "0"+ (month + 1)
                        }else{
                            (month + 1).toString()
                        }

                        val dOfM = if (dayOfMonth < 10){
                            "0"+ (dayOfMonth)
                        }else{
                            (dayOfMonth).toString()
                        }

                        binding.edtBirthday.setText("$year/${m}/$dOfM")
                    },
                    startYear, startMonth, startDay
                ).show()
            }

        val accountId: Int = SharedPreferencesUtils.getAccountId()
        viewModel.getAccountById(accountId)
            .observe(this) { admin: MutableList<AccountEntity> ->
                if (admin.isNotEmpty()) {
                    binding.edtAdminName.setText(admin[0].account_name)
                    binding.edtBirthday.setText(admin[0].account_birthday)
                    binding.edtPhone.setText(admin[0].account_phone)
                    binding.edtUserName.setText(admin[0].user_name)

                    accountEntity = admin[0]
                }
            }

        binding.txtUpdate.setOnClickListener {
           val isNoEditUsername = accountEntity.user_name.equals(binding.edtUserName.text.toString())
            name = accountEntity.user_name

            if (binding.edtAdminName.text.toString() != "") {
                accountEntity.account_name = binding.edtAdminName.text.toString()
            }

            if (binding.edtUserName.text.toString() != "") {
                accountEntity.account_birthday = binding.edtBirthday.text.toString()
            }

            if (binding.edtUserName.text.toString() != "") {
                accountEntity.account_phone = binding.edtPhone.text.toString()
            }

            if (binding.edtUserName.text.toString() != "" ) {
                accountEntity.user_name = binding.edtUserName.text.toString()
            }

            if (binding.edtPassword.text.toString() != "") {
                accountEntity.password =
                    DataUtil.convertToMD5(binding.edtPassword.text.toString()+ Constant.SECURITY_SALT)
            }

            viewModel.addUserAndCheckExist(this@UpdateAdminInfoActivity, accountEntity, isNoEditUsername)
        }

        viewModel.isDuplicate
            .observe(this) {
                if (it){
                    showToast("username already exists!")
                    accountEntity.user_name = name
                }else{
                    showToast("Account' information was updated successfully!")
                    SharedPreferencesUtils.setAccountName(accountEntity.account_name)
                    backToManager()
                }
            }
    }

    private fun backToManager() {
        openActivity(MainManagerActivity::class.java, true)
    }
}




