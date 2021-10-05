package com.sign.deftpdf.ui.documents_screens

interface OnDocumentDetailListener {
    fun documentDeleted()
    fun documentUpdate(newName: String)
}