package com.sign.deftpdf.ui.documents_screens.documents

import android.os.Bundle
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.R
import com.sign.deftpdf.databinding.FragmentDocumentBinding

class DocumentsFragment : Fragment(R.layout.fragment_document) {

    private val binding by viewBinding(FragmentDocumentBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

}