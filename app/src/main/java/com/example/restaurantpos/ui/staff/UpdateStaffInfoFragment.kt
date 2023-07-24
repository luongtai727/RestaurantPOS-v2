package com.example.restaurantpos.ui.staff

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.restaurantpos.databinding.FragmentUpdateStaffInfoBinding
import com.example.restaurantpos.db.entity.AccountEntity
import com.example.restaurantpos.ui.manager.user.UserViewModel
import com.example.restaurantpos.util.Constant
import com.example.restaurantpos.util.DataUtil
import com.example.restaurantpos.util.SharedPreferencesUtils
import com.example.restaurantpos.util.showToast
import java.util.Calendar


class UpdateStaffInfoFragment : Fragment() {

    private lateinit var viewModel: UserViewModel
    lateinit var binding: FragmentUpdateStaffInfoBinding
    lateinit var  accountEntity: AccountEntity
    val calendar = Calendar.getInstance()
    val startYear = calendar.get(Calendar.YEAR) - 20
    val startMonth = calendar.get(Calendar.MONTH) - 5
    val startDay = calendar.get(Calendar.DAY_OF_MONTH) - 10
    var name: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUpdateStaffInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** Khai báo viewModel --> Dùng phương thức addUser --> set ADD Button */
        viewModel = ViewModelProvider(this).get(UserViewModel::class.java)


        /** Device's Back Button*/
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        /** Cancel/Back */
        binding.txtCancel.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }

        val accountId: Int = SharedPreferencesUtils.getAccountId()
        viewModel.getAccountById(accountId)
            .observe(viewLifecycleOwner) { admin: MutableList<AccountEntity> ->
                if (admin.isNotEmpty()) {
                    binding.edtStaffName.setText(admin[0].account_name)
                    binding.edtBirthday.setText(admin[0].account_birthday)
                    binding.edtPhone.setText(admin[0].account_phone)
                    binding.edtUserName.setText(admin[0].user_name)

                    accountEntity = admin[0]
                }
            }


        binding.imgDate.setOnClickListener {
            DatePickerDialog(
                requireContext(),
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

        /** Update Button */
        binding.txtUpdate.setOnClickListener {

            val isNoEditUsername = accountEntity.account_name.equals(binding.edtStaffName.text.toString())
            name = accountEntity.user_name

            if (binding.edtStaffName.text.toString() != "") {
                accountEntity.account_name = binding.edtStaffName.text.toString()
            }

            if (binding.edtUserName.text.toString() != "") {
                accountEntity.account_birthday = binding.edtBirthday.text.toString()
            }

            if (binding.edtUserName.text.toString() != "") {
                accountEntity.account_phone = binding.edtPhone.text.toString()
            }

            if (binding.edtUserName.text.toString() != "") {
                accountEntity.user_name = binding.edtUserName.text.toString()
            }

            if (binding.edtPassword.text.toString() != "") {
                accountEntity.password =
                    DataUtil.convertToMD5(binding.edtPassword.text.toString()+ Constant.SECURITY_SALT)
            }
            viewModel.addUserAndCheckExist(requireContext(), accountEntity, isNoEditUsername)

        }

        viewModel.isDuplicate
            .observe(viewLifecycleOwner) {
                if (it){
                    showToast("username already exists!")
                    accountEntity.user_name = name
                }else{
                    showToast("Account' information was updated successfully!")

                    SharedPreferencesUtils.setAccountName(accountEntity.account_name)

                    findNavController().popBackStack()
                }
            }

        // Value = 1 --> Receptionist gửi.
        // Value = 2 --> Kitchen gửi.
        // Value thì mình chọn thôi

        /*
          1. Của User
          2. Của Kitchen
           */
        if (requireArguments().getInt("updateStaffInfo", 1) == 1) {

        }

        if (requireArguments().getInt("updateStaffInfo", 1) == 2) {

        }
    }

    fun showToast(string: String) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
    }
}