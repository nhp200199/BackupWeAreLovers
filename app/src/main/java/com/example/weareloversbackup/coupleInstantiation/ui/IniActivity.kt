package com.example.weareloversbackup.coupleInstantiation.ui

import android.R
import android.app.AlarmManager
import android.app.DatePickerDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.weareloversbackup.coupleInstantiation.domain.CoupleInstantiationViewModel
import com.example.weareloversbackup.databinding.ActivityIniBinding
import com.example.weareloversbackup.ui.MainActivity
import com.example.weareloversbackup.ui.base.BaseActivity
import com.example.weareloversbackup.utils.helper.IAlarmHelper
import com.example.weareloversbackup.utils.helper.IPermissionHelper
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
    @Inject lateinit var permissionHelper: IPermissionHelper
    private val notificationPermissionListener = object : IPermissionHelper.PermissionListener {
        override fun onPermissionGranted() {
            moveToMainActivity()
        }

        override fun onPermissionDenied(deniedPermissions: List<String>) {
            Toast.makeText(
                this@IniActivity,
                "You can change notification later",
                Toast.LENGTH_SHORT
            ).show()
            moveToMainActivity()
        }
    }

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
                        moveToMainActivity()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionHelper.registerPermissionListener(REQ_PERMISSION_NOTIFICATION, notificationPermissionListener)
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
                checkNotificationPermission()
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

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                permissionHelper.isPermissionGranted(this, android.Manifest.permission.POST_NOTIFICATIONS) -> {
                    moveToMainActivity()
                }
                shouldShowRequestPermissionRationale(android.Manifest.permission.POST_NOTIFICATIONS) -> {
                    //TODO: replace toast with an information dialog
                    Toast.makeText(this, "We need this permission to show notification", Toast.LENGTH_SHORT).show()
                    moveToMainActivity()
                }
                else -> {
                    requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), REQ_PERMISSION_NOTIFICATION)
                }
            }
        } else {
            moveToMainActivity()
        }
    }

    private fun moveToMainActivity() {
        MainActivity.startActivity(this)
        finish()
    }

    private fun scheduleCoupleAlarm() {
        val scheduleResult = alamHelper.scheduleCoupleAlarm(this)
        if (scheduleResult == 1) {
            checkNotificationPermission()
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
                checkNotificationPermission()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQ_PERMISSION_NOTIFICATION) {
            permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults)
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            unregisterReceiver(alarmPermissionReceiver)
        } catch (e: Exception) {
            //just swallow the exception
        }

        permissionHelper.unregisterPermissionListener(REQ_PERMISSION_NOTIFICATION, notificationPermissionListener)
    }

    companion object {
        const val REQ_PERMISSION_NOTIFICATION = 1
    }
}