package com.sign.deftpdf.ui.request_signature

import com.sign.deftpdf.model.BaseModel
import com.sign.deftpdf.model.documents.DocumentsModel
import com.sign.deftpdf.model.sign_link.SignLinkModel

interface RequestLinkView {
    fun requestSuccess(data: SignLinkModel)
}