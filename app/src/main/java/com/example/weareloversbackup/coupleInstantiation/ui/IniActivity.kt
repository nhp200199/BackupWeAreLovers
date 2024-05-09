package com.example.weareloversbackup.coupleInstantiation.ui

import android.R
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weareloversbackup.coupleInstantiation.domain.CoupleInstantiationViewModel
import com.example.weareloversbackup.databinding.ActivityIniBinding
import com.example.weareloversbackup.ui.MainActivity
import com.example.weareloversbackup.ui.base.BaseActivity
import com.example.weareloversbackup.utils.helper.IAlarmHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class IniActivity @Inject constructor() : BaseActivity<ActivityIniBinding>() {
    private val viewModel: CoupleInstantiationViewModel by viewModels()
    @Inject lateinit var alamHelper: IAlarmHelper
    private val alarmPermissionReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d(getClassTag(), "onReceive: ${intent?.action}")
            if (intent?.action == AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED) {
                viewModel.isAlarmSettingsOpened = false

                val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.let {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        Log.d(getClassTag(), "onReceive: ${alarmManager.canScheduleExactAlarms()}")
                        alamHelper.scheduleCoupleAlarm(context, forceInexact = !it.canScheduleExactAlarms())
                        MainActivity.startActivity(context)
                        this@IniActivity.finish()
                    }
                }
            }
        }
    }

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
            saveCoupleData()
            if (viewModel.isAlarmSettingsOpened) {
                Toast.makeText(this, "You can change this later in settings", Toast.LENGTH_SHORT).show()
                alamHelper.scheduleCoupleAlarm(this, forceInexact = true)
                MainActivity.startActivity(this)
                finish()
            } else {
                scheduleCoupleAlarm()
            }
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

    private fun scheduleCoupleAlarm() {
        val scheduleResult = alamHelper.scheduleCoupleAlarm(this)
        if (scheduleResult == 1) {
            MainActivity.startActivity(this)
            finish()
        } else {
            showAlarmPermissionGuideDialog()
        }
    }

    private fun showAlarmPermissionGuideDialog() {
        AlertDialog.Builder(this)
            .setTitle("Alarm Permission Required")
            .setMessage("We need this permission to schedule alarm")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                openSettingsForExactAlarmPermission()
                registerReceiverForAlarmPermission()
            }
            .setNegativeButton("Later") { dialog, _ ->
                dialog.dismiss()
                Toast.makeText(this, "You can change this later in settings", Toast.LENGTH_SHORT).show()
                alamHelper.scheduleCoupleAlarm(this, forceInexact = true)
                MainActivity.startActivity(this)
                finish()
            }
            .create().apply {
                show()
            }

    }

    private fun registerReceiverForAlarmPermission() {
        val intentFilter = IntentFilter(AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED)
        registerReceiver(alarmPermissionReceiver, intentFilter)
    }

    private fun openSettingsForExactAlarmPermission() {
        viewModel.isAlarmSettingsOpened = true
        Intent().also { intent ->
            intent.action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
            startActivity(intent)
        }
    }

    private fun saveCoupleData() {
        viewModel.saveYourName()
        viewModel.saveYourPartnerName()
        viewModel.saveCoupleDate()
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

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(alarmPermissionReceiver)
        } catch (e: Exception) {
            //just swallow the exception
        }
    }
}