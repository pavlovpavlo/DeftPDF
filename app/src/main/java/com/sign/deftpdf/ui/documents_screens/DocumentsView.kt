package com.sign.deftpdf.ui.documents_screens

import com.sign.deftpdf.model.documents.DocumentsModel

interface DocumentsView {
    fun requestSuccess(data: DocumentsModel)
}