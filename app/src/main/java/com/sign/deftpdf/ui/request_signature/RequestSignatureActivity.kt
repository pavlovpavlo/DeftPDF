package com.sign.deftpdf.ui.request_signature

import android.os.Bundle
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.R
import com.sign.deftpdf.base.BaseActivity
import com.sign.deftpdf.databinding.ActivityRequestSignatureBinding
import com.sign.deftpdf.model.documents.DocumentData

class RequestSignatureActivity : BaseActivity(R.layout.activity_request_signature) {

    internal val binding by viewBinding(ActivityRequestSignatureBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data: DocumentData = intent.getSerializableExtra("document") as DocumentData
    }
}