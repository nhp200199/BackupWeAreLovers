package com.example.weareloversbackup.main.home.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.weareloversbackup.R
import com.example.weareloversbackup.databinding.FragmentMainBinding
import com.example.weareloversbackup.main.home.domain.MainFragmentViewModel
import com.example.weareloversbackup.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {
    private val viewModel: MainFragmentViewModel by viewModels()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_settings, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                navigateSettingScreen()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateSettingScreen() {
//        startActivity(Intent(requireContext(), SettingActivity::class.java))
        //TODO: implement settings screen
    }

    private fun setUserInfo(userInfoState: UserInfoUiState) {
        binding.tvYourName.text = userInfoState.yourName
        binding.tvYourFrName.text = userInfoState.yourFrName

//        val calendar = Calendar.getInstance()
//        var dateStartString = userInfoState.coupleDate
//        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy")
//        val dateStart = simpleDateFormat.parse(dateStartString)
//        val dateEndString = simpleDateFormat.format(calendar.time)
//        val dateEnd = simpleDateFormat.parse(dateEndString)
//        val diff = dateEnd.time - dateStart.time
        binding.tvDayCount.text = userInfoState.coupleDate

        Glide.with(requireActivity())
            .load(userInfoState.yourImage)
            .into(binding.profileImage)
        Glide.with(requireActivity())
            .load(userInfoState.yourFrImage)
            .into(binding.friendProfileImage)
    }

    override fun getClassTag(): String {
        return MainFragment::class.java.simpleName
    }

    override fun getViewBindingClass(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }

    override fun setupView() {
        setHasOptionsMenu(true)

        val zoomin = AnimationUtils.loadAnimation(activity, R.anim.zoom_in)
        binding.imgHeart.startAnimation(zoomin)
    }

    override fun setViewListener() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.userInfoUiStateFlow.collect {
                    setUserInfo(it)
                }
            }
        }
    }
}