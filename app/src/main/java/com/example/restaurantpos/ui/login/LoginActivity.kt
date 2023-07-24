package com.example.restaurantpos.ui.login

import android.view.LayoutInflater
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.restaurantpos.base.BaseActivity
import com.example.restaurantpos.databinding.ActivityLoginBinding
import com.example.restaurantpos.util.Constant
import com.example.restaurantpos.util.DataUtil
import com.example.restaurantpos.util.show
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private lateinit var viewModel: LoginViewModel

    override fun initOnCreate() {
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        initListener()
    }

    private fun initListener() {

        DataUtil.setEditTextWithoutSpecialCharactersAndSpaces(
            binding.edtUsername,
            binding.txtInformLogin
        )
        DataUtil.setEditTextWithoutSpecialCharactersAndSpaces(
            binding.edtPassword,
            binding.txtInformLogin
        )

        binding.txtLogin.setOnClickListener {
            // Nếu Empty
            if (binding.edtUsername.text.toString().trim().isEmpty() ||
                binding.edtPassword.text.toString().trim().isEmpty()
            ) {
                CoroutineScope(Dispatchers.Main).launch { showLoginInform("Username & password must not be empty!") }
            } else
            if (binding.edtUsername.text.length >= 3 && binding.edtPassword.text.length >= 3){
                checkLogin(
                    binding.txtInformLogin,
                    binding.edtUsername.text.toString(),
                    DataUtil.convertToMD5(binding.edtPassword.text.toString() + Constant.SECURITY_SALT)
                )
            }
            else
            {
                CoroutineScope(Dispatchers.Main).launch { showLoginInform("Username & password \n needs to consist of 3 to 14 characters!") }
            }

        }
    }


    private fun showLoginInform(msg: String) {
        binding.txtInformLogin.text = msg
        binding.txtInformLogin.show()
    }

    private fun checkLogin(txtInform: TextView, userName: String, password: String) {
        viewModel.checkLogin(this@LoginActivity, txtInform, userName, password)
    }

    // End
    override fun getInflaterViewBinding(layoutInflater: LayoutInflater): ActivityLoginBinding {
        return ActivityLoginBinding.inflate(layoutInflater)
    }
}