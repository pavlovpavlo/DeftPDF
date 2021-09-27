package com.sign.deftpdf.ui.notification

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.DeftApp
import com.sign.deftpdf.R
import com.sign.deftpdf.databinding.ActivityCheckAuthBinding
import com.sign.deftpdf.databinding.FragmentAccountSettingsBinding
import com.sign.deftpdf.databinding.FragmentChangePasswordBinding
import com.sign.deftpdf.databinding.FragmentNotificationsBinding
import com.sign.deftpdf.model.notifications.NotificationsModel
import com.sign.deftpdf.ui.main.MainActivity

class NotificationFragment : Fragment(R.layout.fragment_notifications), NotificationsView {

    private val binding by viewBinding(FragmentNotificationsBinding::bind)
    private lateinit var presenter: NotificationsPresenter
    private lateinit var activity: MainActivity
    private lateinit var navController: NavController

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            activity = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = NotificationsPresenter(activity)
        presenter.attachView(this)
        presenter.sendResponse(DeftApp.user.apiToken.toString())
        navController = NavHostFragment.findNavController(this@NotificationFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backBtn.setOnClickListener { navController.popBackStack() }
    }

    override fun getNotificationsSuccess(data: NotificationsModel) {
        binding.notifications.layoutManager = LinearLayoutManager(requireContext())
        data.data?.let {
            binding.notifications.adapter = NotificationsAdapter(it)
        }
    }

}