package com.sign.deftpdf.ui.help

import com.sign.deftpdf.model.documents.DocumentsModel
import com.sign.deftpdf.model.faq.FaqModel

interface HelpView {
    fun requestSuccess(data: FaqModel)
}