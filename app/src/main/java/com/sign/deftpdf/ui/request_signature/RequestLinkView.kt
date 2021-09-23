package com.sign.deftpdf.ui.request_signature

import com.sign.deftpdf.model.BaseModel
import com.sign.deftpdf.model.documents.DocumentsModel

interface RequestEmailView {
    fun requestSuccess(data: BaseModel)
}