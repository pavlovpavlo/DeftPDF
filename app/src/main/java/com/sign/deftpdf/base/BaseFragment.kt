package com.sign.deftpdf.base

import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.PopupWindow
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sign.deftpdf.R
import com.sign.deftpdf.ui.main.MainActivity

open class BaseFragment(layout: Int) : Fragment(layout) {

    private lateinit var popup: PopupWindow

    fun baseSettingsDialog(layout: Int): View {
        val activity = requireActivity() as MainActivity
        val inflater = requireActivity().getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(layout, null)
        popup = PopupWindow(view, RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT, true)
        popup.animationStyle = R.style.popup_window_animation_phone
        popup.isOutsideTouchable = true
        Handler(Looper.getMainLooper()).postDelayed({ activity.binding.dialogBack.visibility = View.VISIBLE }, 150L)

        popup.setOnDismissListener {
            Handler(Looper.getMainLooper()).postDelayed({ activity.binding.dialogBack.visibility = View.INVISIBLE }, 150L)
        }
        popup.showAtLocation(activity.binding.appTablayout, Gravity.BOTTOM, 0, 0)

        return view;
    }

    fun openMenu(){
        val activity = requireActivity() as MainActivity
        activity.mDrawer.open()
    }

    fun showSortPopup(): View {
        return baseSettingsDialog(R.layout.dialog_sort)
    }

    fun showFilterPopup(): View {
        return baseSettingsDialog(R.layout.dialog_filter)
    }

    fun showDocumentDetailPopup(): View {
        return baseSettingsDialog(R.layout.dialog_document_menu)
    }

    fun dismissPopup() {
        popup.dismiss()
    }
}