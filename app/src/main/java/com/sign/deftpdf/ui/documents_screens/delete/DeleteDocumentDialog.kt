package com.sign.deftpdf.ui.documents_screens.delete

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.sign.deftpdf.R
import com.sign.deftpdf.ui.documents_screens.rename.RenameDocumentDialog
import com.sign.deftpdf.util.NoInternetConnectionUtil
import java.io.UnsupportedEncodingException

class DeleteDocumentDialog : DialogFragment() {

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window!!.setLayout(width, height)
            dialog.setCanceledOnTouchOutside(false)
            dialog.setCancelable(true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppTheme_FullScreenDialogLight)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.dialog_delete_file, container, false)

        val confirm : AppCompatButton = view.findViewById(R.id.confirm)
        val cancel : AppCompatButton = view.findViewById(R.id.cancel)
        val name : TextView = view.findViewById(R.id.name)

        name.text = "${name.text} $docName"
        cancel.setOnClickListener { dismiss() }
        confirm.setOnClickListener {
            confirmFunction.invoke()
            dismiss()
        }

        return view
    }

    companion object {
        private const val TAG = "DeleteDocumentDialog"
        private var dialog: DeleteDocumentDialog? = null
        lateinit var docName: String
        private lateinit var confirmFunction: () -> Unit
        fun display(
            fragmentManager: FragmentManager,
            documentName: String,
            refreshResponse: () -> Unit
        ): DeleteDocumentDialog? {
            docName = documentName
            confirmFunction = refreshResponse
            dialog = DeleteDocumentDialog()
            fragmentManager.executePendingTransactions()
            dialog!!.show(fragmentManager, TAG)
            return dialog
        }

        fun dismissCurrent() {
            dialog!!.dismiss()
        }
    }
}