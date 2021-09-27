package com.sign.deftpdf.ui.draw

import android.graphics.Bitmap
import com.sign.deftpdf.custom_views.signature.SignatureUtils

interface OnDrawSaveListener {
    fun saveImageBitmap(bitmap: Bitmap)
    fun saveImageViewHolder(viewHolder: SignatureUtils.ViewHolder)
}