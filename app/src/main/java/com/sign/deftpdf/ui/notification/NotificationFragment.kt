package com.sign.deftpdf.ui.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.R
import com.sign.deftpdf.databinding.ActivityCheckAuthBinding
import com.sign.deftpdf.databinding.FragmentAccountSettingsBinding
import com.sign.deftpdf.databinding.FragmentChangePasswordBinding
import com.sign.deftpdf.databinding.FragmentNotificationsBinding

class NotificationFragment : Fragment(R.layout.fragment_notifications) {

    private val binding by viewBinding(FragmentNotificationsBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}