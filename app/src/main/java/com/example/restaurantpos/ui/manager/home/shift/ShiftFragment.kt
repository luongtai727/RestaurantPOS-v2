package com.example.restaurantpos.ui.manager.home.shift

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.restaurantpos.R
import com.example.restaurantpos.databinding.FragmentShiftBinding
import com.example.restaurantpos.db.entity.AccountEntity
import com.example.restaurantpos.db.entity.AccountShiftEntity
import com.example.restaurantpos.ui.manager.user.UserViewModel
import com.example.restaurantpos.util.DataUtil
import com.example.restaurantpos.util.SharedPreferencesUtils
import com.example.restaurantpos.util.gone
import com.example.restaurantpos.util.hide
import com.example.restaurantpos.util.show
import java.util.Calendar


class ShiftFragment : Fragment() {
    private lateinit var binding: FragmentShiftBinding
    private lateinit var adapterShift: ShiftAdapter
    private lateinit var accountShiftAdapter: AccountShiftAdapter
    private lateinit var adapterStaffSelection: StaffSelectionAdapter

    private lateinit var viewModelShift: ShiftViewModel
    private lateinit var viewModelUser: UserViewModel

    private var year = 2023
    private var month = 7
    private var day = 10

    // Add account_shift
    lateinit var dialog: AlertDialog
    val calendar = Calendar.getInstance()

    //  Xử lý role trong spinner
    private var shiftName = 1

    private var staffObject: AccountEntity? = null
    private var listAccountActive = ArrayList<AccountEntity>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShiftBinding.inflate(inflater, container, false)
        return binding.root
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /** ViewModel */
        viewModelShift = ViewModelProvider(this).get(ShiftViewModel::class.java)
        viewModelUser = ViewModelProvider(this).get(UserViewModel::class.java)

        /**---------------------------------------------------------------------------------------*/
        // Code for add Account_Shift
        binding.txtAddAccountShift.setOnClickListener {
            showAddAccountShiftDialogTop()
        }
        /**---------------------------------------------------------------------------------------*/
        // Vừa vào là đã phải hiển thị NOW TIME (year + month + day) rồi nhé
        binding.txtDateInShift.text = "$year/$month"

        // Trả về hôm nay, và các ngày tiếp theo được xử lý tự động
        day = getFirst()


        /** imgBack */
        binding.imgBack.setOnClickListener {
            findNavController().popBackStack()
        }
        /** imgMonthBack */
        binding.imgMonthBack.setOnClickListener {
            backWeek()
        }

        /** imgMonthNext */
        binding.imgMonthNext.setOnClickListener {
            nextWeek()
        }

        /**---------------------------------------------------------------------------------------*/
        /** Adapter */
        adapterShift = ShiftAdapter(
            requireContext(),
            viewLifecycleOwner,
            ArrayList(),
            year,
            month,
            day,
            object : ShiftAdapter.EventClickShiftListener {
                override fun clickMorningShift(shift_id: String) {
                    showDeleteAccountShiftDialog(shift_id)
                }

                override fun clickAfternoonShift(shift_id: String) {
                    showDeleteAccountShiftDialog(shift_id)
                }

                override fun clickNightShift(shift_id: String) {
                    showDeleteAccountShiftDialog(shift_id)
                }
            })

        binding.rcyShift.adapter = adapterShift
        /**---------------------------------------------------------------------------------------*/
    }


    /** Message check xem AccountShift đã tồn tại hay chưa */
    private fun showMessage(content: String) {
        Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
    }


    /** Add Accouont_Shift Dialog- ON TOP*/
    private val startYear = calendar.get(Calendar.YEAR)
    private val startMonth = calendar.get(Calendar.MONTH)
    private val startDay = calendar.get(Calendar.DAY_OF_MONTH)
    private var accountID: Int = 0
    private var shift_name: Int = 0
    private var shiftID = ""

    @SuppressLint("SetTextI18n")
    private fun showAddAccountShiftDialogTop() {

        // -----------------Prepare--------------------------------------------------//
        // 1.  Build Dialog
        // 2.  Designed XML --> View
        // 3.  Set VIEW tra ve above --> Dialog
        val build = AlertDialog.Builder(requireActivity(), R.style.ThemeCustom)
        val view = layoutInflater.inflate(R.layout.dialog_alert_add_account_shift_on_top, null)
        build.setView(view)
        // 4.  Get Component of Dialog
//        val edtAccount = view.findViewById<EditText>(R.id.edtAccount)
        val spnShift = view.findViewById<Spinner>(R.id.spnShift)
        val txtShiftDate = view.findViewById<TextView>(R.id.txtShiftDate)
        val txtError = view.findViewById<TextView>(R.id.txtError)

        val imgClose = view.findViewById<ImageView>(R.id.imgClose)
        val imgDate = view.findViewById<ImageView>(R.id.imgDate)

        val btnAdd = view.findViewById<Button>(R.id.btnAdd)
        val btnCancel = view.findViewById<Button>(R.id.btnCancel)

        val rcyAccountInner = view.findViewById<RecyclerView>(R.id.rcyAccountInner)


        // -----------------Code for Component----------------------------------------//
        /** 1.  Xử lý role trong spinner  */
        handleShiftNameBySpinner(spnShift)
        /** 2.  Xử lý selectAccount and get account_id for account_shift */
        // 2.1 Xử lý adpater cho rcyAccountInner
        adapterStaffSelection = StaffSelectionAdapter(
            requireParentFragment(),
            ArrayList(),
            object : StaffSelectionAdapter.EventClickStaffListener {
                override fun clickStaff(itemStaff: AccountEntity) {
                    staffObject = itemStaff
//                    edtAccount.setText(itemStaff.account_name)
                    accountID = itemStaff.account_id
                }
            })
        rcyAccountInner.adapter = adapterStaffSelection
        // ------------------------------------------------------------


        // 2.2 Xử lý doOnTextChanged cho accountName
        val spinner = view.findViewById<Spinner>(R.id.spn_customer)
        // Set data cho Spinner. Này chính là Combobox mà Anh Mạnh nói
        viewModelUser.getAllUserActive().observe(viewLifecycleOwner){
            // Tạo Adapter và gắn dữ liệu vào Spinner. it Chính là Acccount toString.
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, it)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
        }

        // Bắt sự kiện khi chọn dữ liệu trên Spinner
        // Thằng dưới này chính là nguyên nhân dẫn đến mặt định "Thằng đầu tiên" trong spinner khi tìm kiếm
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val account = parent?.getItemAtPosition(position) as AccountEntity
                // For Shift_id
                accountID = account.account_id
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Xử lý khi không chọn gì cả trên Spinner
            }
        }

        /** ------------------------------------------------------------ */

        // 3. Pick-up-Date
        imgDate.setOnClickListener {
//            edtAccount.clearFocus()
            DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    txtShiftDate.text = "$year/${1 + month}/$dayOfMonth"
                },
                startYear, startMonth, startDay
            ).show()
        }


        // 4.  Add Account_Shift
        // Check account đã tồn tại hay chưa
        viewModelShift.isDuplicate.observe(viewLifecycleOwner) { isDuplicate ->
            txtError.hide()
            if (isDuplicate) {
                txtError.text = "The staff you have selected \n is already assigned to this shift!"
                txtError.show()
//                showMessage("Duplication")
            } else {
                dialog.dismiss()
            }
        }


        btnAdd.setOnClickListener {
            shiftID = txtShiftDate?.text.toString() + "  $shiftName"

            if (txtShiftDate.text.isNotEmpty()  && accountID != 0) {
                viewModelShift.addAccountShift(
                    AccountShiftEntity(0, shiftID, accountID)
                )
//                dialog.dismiss()
            } else {
                txtError.text = "The staff or date you have selected \n may not be accurate!"
                txtError.show()
            }
        }

        // Other:  Dau X  &   Cancel Button
        imgClose.setOnClickListener { dialog.dismiss() }
        btnCancel.setOnClickListener { dialog.dismiss() }

        // End: Tao Dialog (Khi khai bao chua thuc hien) and Show len display
        dialog = build.create()
        dialog.show()
    }



    /** handleShiftNameBySpinner */
    private fun handleShiftNameBySpinner(spnShift: Spinner) {
        val listShiftName = listOf("Morning", "Afternoon", "Night")

        spnShift.adapter = ShiftSpinnerAdapter(requireActivity(), listShiftName)
        spnShift.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                shiftName = position + 1
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    /** Add Accouont_Shift Dialog */
    // Copy từ NewOrderFragment sang
    private fun showDeleteAccountShiftDialog(shift_id: String) {
// -----------------Prepare--------------------------------------------------//
        // 1.  Build Dialog
        // 2.  Designed XML --> View
        // 3.  Set VIEW tra ve above --> Dialog
        val build = AlertDialog.Builder(requireActivity(), R.style.ThemeCustom)
        val view = layoutInflater.inflate(R.layout.dialog_shift, null)
        build.setView(view)
        // 4.  Get Component of Dialog
        val imgClose = view.findViewById<ImageView>(R.id.img_dialog_close)

        val rcyStaffSelection = view.findViewById<RecyclerView>(R.id.rcv_detail_shift)
        rcyStaffSelection.setHasFixedSize(true)

        accountShiftAdapter = AccountShiftAdapter(
            ArrayList(),
            object : AccountShiftAdapter.EventClickListener{
                override fun onClickDelete(shift_id: Int) {
                    viewModelShift.deleteAccountShift(shift_id)
                    dialog.dismiss()
                }
            })

        rcyStaffSelection.adapter = accountShiftAdapter

        viewModelShift.getListAccountShiftForSetListData(shift_id).observe(viewLifecycleOwner){
            if (!it.isEmpty()){
                accountShiftAdapter.setListData(it)
            }
        }

        imgClose.setOnClickListener { dialog.dismiss() }
        // End: Tao Dialog (Khi khai bao chua thuc hien) and Show len display
        dialog = build.create()
        dialog.show()
    }

    private fun backWeek() {
        // Tính lại Năm, Tháng, ngày. Và Set lại data cho adapter
        if (7 >= day) {
            if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
                if (month == 1) {
                    month = 12
                    year -= 1
                } else {
                    month -= 1
                }
                day = DataUtil.numberOfDayInAMonthOfLeapYear[month - 1] + (day - 7)
            } else {
                if (month == 1) {
                    month = 12
                    year -= 1
                } else {
                    month -= 1
                }
                day = DataUtil.numberOfDayInAMonthOfNotLeapYear[month - 1] + (day - 7)
            }
        } else {
            day -= 7
        }
        binding.txtDateInShift.text = "$year/$month"
        adapterShift.setDate(year, month, day)
    }

    private fun nextWeek() {
        val now = day + 7

        if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) {
            if (now > DataUtil.numberOfDayInAMonthOfLeapYear[month]) {
                day = now - DataUtil.numberOfDayInAMonthOfLeapYear[month]
                if (month == 12) {
                    month = 1
                    year += 1
                } else {
                    month += 1
                }
            } else {
                day = now
            }
        } else {
            if (now > DataUtil.numberOfDayInAMonthOfNotLeapYear[month]) {
                day = now - DataUtil.numberOfDayInAMonthOfNotLeapYear[month]
                if (month == 12) {
                    month = 1
                    year += 1
                } else {
                    month += 1
                }
            } else {
                day = now
            }
        }
        binding.txtDateInShift.text = "$year/$month"
        adapterShift.setDate(year, month, day)
    }

    /** getFirst --> Monday is ? */
    private fun getFirst(): Int {
        val calendar = Calendar.getInstance()
        val nowDay = calendar.get(Calendar.DATE)
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        // 1 là Chủ Nhật và 2 đến 7 là tương ứng
        // Xử lý để thứ 2 là ngày đầu tuần

        when (dayOfWeek) {
            1 -> {
                return nowDay - 6
            }

            2 -> {
                return nowDay
            }

            3 -> {
                return nowDay - 1
            }

            4 -> {
                return nowDay - 2
            }

            5 -> {
                return nowDay - 3
            }

            6 -> {
                return nowDay - 4
            }

            7 -> {
                return nowDay - 5
            }

        }
        return 1
    }
}