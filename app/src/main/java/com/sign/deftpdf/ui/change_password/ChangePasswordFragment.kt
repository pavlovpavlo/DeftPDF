package com.sign.deftpdf.ui.change_password

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.DeftApp
import com.sign.deftpdf.R
import com.sign.deftpdf.databinding.FragmentChangePasswordBinding
import com.sign.deftpdf.model.user.UserModel
import com.sign.deftpdf.ui.main.MainActivity

class ChangePasswordFragment : Fragment(R.layout.fragment_change_password), ChangePasswordView {

    private val binding by viewBinding(FragmentChangePasswordBinding::bind)
    private lateinit var presenter: ChangePasswordPresenter
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = ChangePasswordPresenter((activity as MainActivity))
        presenter.attachView(this)
        navController = NavHostFragment.findNavController(this@ChangePasswordFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            changePassword.setOnClickListener { changePassword() }
            passShower.setOnClickListener { visibilityPassword() }
            passRepeatShower.setOnClickListener { visibilityPasswordRepeat() }
        }
    }

    private fun changePassword() {
        if (checkData())
            presenter.sendResponse(DeftApp.user.apiToken!!, binding.passEdittext.text.toString())
    }

    private fun checkData(): Boolean {
        with(binding) {
            if (passEdittext.text.isNullOrEmpty())
                return false
            if (passEdittextRepeat.text.isNullOrEmpty())
                return false
            if (passEdittextRepeat.text.toString() != passEdittext.text.toString())
                return false
        }
        return true
    }

    private fun visibilityPassword() {
        with(binding) {
            if (passShower.isChecked)
                passEdittext.transformationMethod = HideReturnsTransformationMethod()
            else
                passEdittext.transformationMethod = PasswordTransformationMethod()
        }
    }

    private fun visibilityPasswordRepeat() {
        with(binding) {
            if (passRepeatShower.isChecked)
                passEdittextRepeat.transformationMethod = HideReturnsTransformationMethod()
            else
                passEdittextRepeat.transformationMethod = PasswordTransformationMethod()
        }
    }

    override fun passwordChangeSuccess(data: UserModel) {
        navController.popBackStack()
    }

}