package com.example.weareloversbackup.coupleInstantiation.ui

import android.R
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weareloversbackup.coupleInstantiation.domain.CoupleInstantiationViewModel
import com.example.weareloversbackup.databinding.ActivityIniBinding
import com.example.weareloversbackup.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date

@AndroidEntryPoint
class IniActivity : BaseActivity<ActivityIniBinding>() {
    private val viewModel: CoupleInstantiationViewModel by viewModels()

    override fun getClassTag(): String {
        return this::class.java.simpleName
    }

    override fun getViewBindingClass(inflater: LayoutInflater): ActivityIniBinding {
        return ActivityIniBinding.inflate(inflater)
    }

    override fun setupView() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.coupleInstantiationUIState.collectLatest {
                    binding.btnConfirm.isEnabled = it.isFormValid
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.coupleDateStateFlow.collectLatest {
                    binding.edtDate.setText(it)
                }
            }
        }
    }

    override fun setViewListener() {
        binding.btnConfirm.setOnClickListener {
            Log.d(getClassTag(), "on button confirm clicked")
        }

        binding.edtYourName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setYourNameInput(binding.edtYourName.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        binding.edtYourFrName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setYourPartnerNameInput(binding.edtYourFrName.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.edtDate.setOnClickListener {
            showDatePicker()
        }
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        val year = cal[Calendar.YEAR]
        val month = cal[Calendar.MONTH]
        val day = cal[Calendar.DAY_OF_MONTH]
        val datePickerDialog = DatePickerDialog(this,
            R.style.ThemeOverlay_DeviceDefault_Accent_DayNight,
            { view, year, month, dayOfMonth -> //because the month is counted from 0
                var month = month
                month = month + 1
                val date = "$dayOfMonth/$month/$year"
                viewModel.setCoupleDate(date)
            }, year, month, day
        )
        datePickerDialog.datePicker.maxDate = Date().time
        datePickerDialog.show()
    }
}