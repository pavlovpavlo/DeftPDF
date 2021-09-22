package com.sign.deftpdf.ui.account

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

class DeleteSignedDialog : DialogFragment() {

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
        val view: View = inflater.inflate(R.layout.dialog_delete_signature, container, false)

        val confirm : AppCompatButton = view.findViewById(R.id.confirm)
        val cancel : AppCompatButton = view.findViewById(R.id.cancel)

        cancel.setOnClickListener { dismiss() }
        confirm.setOnClickListener {
            confirmFunction.invoke()
            dismiss()
        }

        return view
    }

    companion object {
        private const val TAG = "DeleteSignedDialog"
        private var dialog: DeleteSignedDialog? = null
        private lateinit var confirmFunction: () -> Unit
        fun display(
            fragmentManager: FragmentManager,
            refreshResponse: () -> Unit
        ): DeleteSignedDialog? {
            confirmFunction = refreshResponse
            dialog = DeleteSignedDialog()
            fragmentManager.executePendingTransactions()
            dialog!!.show(fragmentManager, TAG)
            return dialog
        }

        fun dismissCurrent() {
            dialog!!.dismiss()
        }
    }
}