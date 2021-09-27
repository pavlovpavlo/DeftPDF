package com.sign.deftpdf.ui.documents_screens.document

import com.sign.deftpdf.model.document.DocumentModel
import com.sign.deftpdf.model.documents.DocumentsModel

interface GetDocumentView {
    fun requestSuccess(data: DocumentModel)
}