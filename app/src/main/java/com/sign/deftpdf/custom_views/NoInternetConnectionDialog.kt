package com.sign.deftpdf.custom_views

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.sign.deftpdf.R
import com.sign.deftpdf.util.NoInternetConnectionUtil
import java.io.UnsupportedEncodingException

class NoInternetConnectionDialog : DialogFragment() {
    private var isReloadClose = false

    override fun onDestroy() {
        if (!isReloadClose) {
            val intent = Intent(Intent.ACTION_MAIN)
            intent.addCategory(Intent.CATEGORY_HOME)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)
            requireActivity().finish()
        }
        super.onDestroy()
    }

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
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        isReloadClose = false
        val view: View = inflater.inflate(R.layout.layout_no_internet, container, false)
        view.findViewById<View>(R.id.reload).setOnClickListener {
            if (NoInternetConnectionUtil().isInternetOn(requireContext())) {
                try {
                    isReloadClose = true
                    onRefreshResponse.invoke()
                } catch (e: UnsupportedEncodingException) {
                    e.printStackTrace()
                }
                dismiss()
            }
        }
        return view
    }

    companion object {
        const val TAG = "NoInternerConnectionDialog"
        var dialog: NoInternetConnectionDialog? = null
        private lateinit var onRefreshResponse: () -> Unit
        fun display(
            fragmentManager: FragmentManager,
            refreshResponse: () -> Unit
        ): NoInternetConnectionDialog? {
            onRefreshResponse = refreshResponse
            dialog = NoInternetConnectionDialog()
            fragmentManager.executePendingTransactions()
            dialog!!.show(fragmentManager, TAG)
            return dialog
        }

        fun dismissCurrent() {
            dialog!!.dismiss()
        }
    }
}