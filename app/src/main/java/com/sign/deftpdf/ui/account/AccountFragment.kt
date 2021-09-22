package com.sign.deftpdf.ui.account

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.DeftApp
import com.sign.deftpdf.R
import com.sign.deftpdf.base.BaseFragment
import com.sign.deftpdf.databinding.*
import com.sign.deftpdf.ui.check_auth.CheckAuthActivity
import com.sign.deftpdf.ui.main.MainActivity
import com.sign.deftpdf.util.LocalSharedUtil
import com.sign.deftpdf.util.Util
import java.util.*

class AccountFragment : BaseFragment(R.layout.fragment_account) {

    private val binding by viewBinding(FragmentAccountBinding::bind)
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = NavHostFragment.findNavController(this@AccountFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signOut.setOnClickListener {
            LocalSharedUtil().setTokenParameter("", requireContext())
            startActivity(Intent(requireContext(), CheckAuthActivity::class.java))
            requireActivity().finishAffinity()
        }
        binding.securityTab.setOnClickListener {
            navController.navigate(R.id.navigation_change_password)
        }
        binding.burgerBtn.setOnClickListener {
            openMenu()
        }
        binding.notificationMenu.setOnClickListener {
            navController.navigate(R.id.navigation_notification)
        }

        val userInitialsImage = view.findViewById<TextView>(R.id.document_image)
        val userInitials = view.findViewById<TextView>(R.id.document_name)
        val userDate = view.findViewById<TextView>(R.id.document_date)

        userInitials.text = DeftApp.user.email
        userDate.text = DeftApp.user.createdAt
        userInitialsImage.text = Util.getInitialsLetter().toUpperCase(Locale.getDefault())
    }
}