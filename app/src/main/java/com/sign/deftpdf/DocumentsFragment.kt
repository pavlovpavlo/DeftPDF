package com.sign.deftpdf

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.databinding.ActivityCheckAuthBinding
import com.sign.deftpdf.databinding.FragmentChangePasswordBinding
import com.sign.deftpdf.databinding.FragmentDocumentBinding
import com.sign.deftpdf.databinding.FragmentHomeBinding

class DocumentsFragment : Fragment(R.layout.fragment_document) {

    private val binding by viewBinding(FragmentDocumentBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}