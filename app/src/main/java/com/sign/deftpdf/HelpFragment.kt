package com.sign.deftpdf

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.databinding.ActivityCheckAuthBinding
import com.sign.deftpdf.databinding.FragmentChangePasswordBinding
import com.sign.deftpdf.databinding.FragmentHelpBinding
import com.sign.deftpdf.databinding.FragmentHomeBinding

class HelpFragment : Fragment(R.layout.fragment_help) {

    private val binding by viewBinding(FragmentHelpBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}