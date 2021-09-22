package com.sign.deftpdf.base

import com.sign.deftpdf.model.BaseModel

interface BaseModelView {
    fun requestSuccess(data: BaseModel)
}