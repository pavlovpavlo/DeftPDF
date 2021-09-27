package com.sign.deftpdf.util

import android.content.Context
import android.net.Uri
import com.sign.deftpdf.DeftApp
import com.sign.deftpdf.api.ApiService
import com.sign.deftpdf.api.RetrofitClient
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

object Util {
    const val BASE_URL = "https://pdf.webstaginghub.com/api/"
    const val DATA_URL = "https://pdf.webstaginghub.com/"
    const val MIMETYPE_PDF = "application/pdf"
    const val MIMETYPE_IMAGES = "image/*"
    var SORT_BY = "updated_at"
    var SORT_DESC = "desc"
    var SORT_ASC = "asc"
    var STATUS_SIGNED = "signed"
    var STATUS_DRAFT = "draft"
    var STATUS_PENDING = "disable"
    var STATUS_ORIGINAL = "original"
    var FILTER_WEEK = "week"
    var FILTER_MONTH = "month"

    fun calculateDate(filter: String): String {
        val calendar = Calendar.getInstance()
        calendar.time = Date()

        if (filter == FILTER_WEEK)
            calendar.add(Calendar.WEEK_OF_MONTH, -1)
        else
            calendar.add(Calendar.MONTH, -1)

        val date = calendar.time
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                date
        )
    }

    fun getFile(mContext: Context?, documentUri: Uri): File {
        val inputStream = mContext?.contentResolver?.openInputStream(documentUri)
        var file =  File("")
        inputStream.use { input ->
            file =
                    File(mContext?.cacheDir, System.currentTimeMillis().toString()+".pdf")
            FileOutputStream(file).use { output ->
                val buffer =
                        ByteArray(4 * 1024) // or other buffer size
                var read: Int = -1
                while (input?.read(buffer).also {
                            if (it != null) {
                                read = it
                            }
                        } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
        }
        return file
    }

    fun getInitialsLetter(): String {
        val initials: List<String>? = DeftApp.user.name?.trim()?.replace(" +", " ")?.split(" ")
        return if (initials != null) {
            var string = ""
            for (value in initials)
                string+=value[0].toString()
            string
        } else
            ""
    }
}