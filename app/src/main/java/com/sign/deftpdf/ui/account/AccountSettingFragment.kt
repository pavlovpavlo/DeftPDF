package com.sign.deftpdf.ui.account

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.R
import com.sign.deftpdf.databinding.FragmentAccountSettingsBinding

class AccountSettingFragment : Fragment(R.layout.fragment_account_settings) {

    private val binding by viewBinding(FragmentAccountSettingsBinding::bind)
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = NavHostFragment.findNavController(this@AccountSettingFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.changePassword.setOnClickListener {
            navController.navigate(R.id.navigation_change_password)
        }
    }

}