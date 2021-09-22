package com.sign.deftpdf.ui.documents_screens.document

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.R
import com.sign.deftpdf.base.BaseActivity
import com.sign.deftpdf.databinding.ActivityDocumentDetailsBinding
import com.sign.deftpdf.model.documents.DocumentData

class DocumentDetailsActivity : BaseActivity(R.layout.activity_document_details) {

    internal val binding by viewBinding(ActivityDocumentDetailsBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data: DocumentData = intent.getSerializableExtra("document") as DocumentData
    }
}