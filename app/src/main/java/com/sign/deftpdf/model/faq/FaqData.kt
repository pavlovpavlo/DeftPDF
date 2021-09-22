package com.sign.deftpdf.model.faq

import com.google.gson.annotations.SerializedName


class FaqData {
    @SerializedName("id")
    var id = 0

    @SerializedName("question")
    var question: String? = null

    @SerializedName("answer")
    var answer: String? = null

    var expanded = false
}