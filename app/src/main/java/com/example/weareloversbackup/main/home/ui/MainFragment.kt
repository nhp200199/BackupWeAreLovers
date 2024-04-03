package com.example.weareloversbackup.main.home.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.app.ActivityCompat.invalidateOptionsMenu
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.weareloversbackup.R
import com.example.weareloversbackup.data.constant.PREF_YOUR_FRIEND_NAME
import com.example.weareloversbackup.data.constant.PREF_YOUR_NAME
import com.example.weareloversbackup.databinding.FragmentMainBinding
import com.example.weareloversbackup.main.home.domain.MainFragmentViewModel
import com.example.weareloversbackup.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : BaseFragment<FragmentMainBinding>() {
    private val viewModel: MainFragmentViewModel by viewModels()

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_settings, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(getClassTag(), "onOptionsItemSelected: ${item.itemId}")
        return when (item.itemId) {
            R.id.menu_action_edit_couple_data -> {
                viewModel.setIsEditingCoupleDate(isVisible)
                true
            }
            R.id.menu_action_edit_background -> {
                true
            }
            R.id.action_save_couple_data -> {
                viewModel.setIsEditingCoupleDate(false)
                viewModel.saveCoupleData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun enableCoupleDataEditor(visible: Boolean) {
        binding.ibEditCoupleDate.isVisible = visible
        binding.ibEditYourName.isVisible = visible
        binding.ibEditYourImage.isVisible = visible
        binding.ibEditYourPartnerName.isVisible = visible
        binding.ibEditYourPartnerImage.isVisible = visible
    }

    private fun navigateSettingScreen() {
//        startActivity(Intent(requireContext(), SettingActivity::class.java))
        //TODO: implement settings screen
    }

    private fun setUserInfo(userInfoState: UserInfoUiState) {
        binding.tvYourName.text = userInfoState.yourName
        binding.tvYourFrName.text = userInfoState.yourFrName

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
        binding.ibEditYourName.setOnClickListener {
            showDialogChangeName(PREF_YOUR_NAME, binding.tvYourName.text.toString())
        }

        binding.ibEditYourPartnerName.setOnClickListener {
            showDialogChangeName(PREF_YOUR_FRIEND_NAME, binding.tvYourFrName.text.toString())
        }
    }

    private fun showDialogChangeName(target: String, currentValue: String) {
        val bundle = Bundle()
        bundle.putString("target", target)
        bundle.putString("name", currentValue)

        val dialogFragment = ChangeNameDialogFragment()
        dialogFragment.arguments = bundle
        dialogFragment.show(parentFragmentManager, "ChangeNameDialogFragment")
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

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isEditingCoupleDataFlow.collect {
                    invalidateOptionsMenu(requireActivity())
                    enableCoupleDataEditor(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.cancelEditCoupleData()
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_settings).isVisible = !viewModel.isEditingCoupleData()
        menu.findItem(R.id.action_save_couple_data).isVisible = viewModel.isEditingCoupleData()
        super.onPrepareOptionsMenu(menu)
    }
}