package com.sign.deftpdf.ui.account

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.sign.deftpdf.DeftApp
import com.sign.deftpdf.R
import com.sign.deftpdf.base.BaseModelView
import com.sign.deftpdf.custom_views.signature.SignatureUtils
import com.sign.deftpdf.databinding.FragmentAccountSettingsBinding
import com.sign.deftpdf.model.BaseModel
import com.sign.deftpdf.model.user.UserModel
import com.sign.deftpdf.ui.draw.DrawActivity
import com.sign.deftpdf.ui.draw.OnDrawSaveListener
import com.sign.deftpdf.ui.main.GetUserPresenter
import com.sign.deftpdf.ui.main.GetUserView
import com.sign.deftpdf.ui.main.MainActivity
import com.sign.deftpdf.util.Util
import java.util.*

class AccountSettingFragment : Fragment(R.layout.fragment_account_settings), GetUserView {

    private val binding by viewBinding(FragmentAccountSettingsBinding::bind)
    private lateinit var navController: NavController
    private lateinit var activity: MainActivity
    private lateinit var thisView: View

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            activity = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = NavHostFragment.findNavController(this@AccountSettingFragment)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thisView = view
        initListeners()
        val presenter = GetUserPresenter(activity)
        presenter.attachView(this)
        presenter.sendResponse(DeftApp.user.apiToken.toString())
    }

    private fun initListeners() {
        with(binding) {
            addSign.setOnClickListener {
                DrawActivity.isSign = true
                DrawActivity.isAccountScreen = true
                DrawActivity.listener = object : OnDrawSaveListener {
                    override fun saveImageBitmap(bitmap: Bitmap) {
                        signImage.setImageBitmap(bitmap)
                    }

                    override fun saveImageViewHolder(viewHolder: SignatureUtils.ViewHolder) {
                    }
                }
                startActivity(Intent(requireContext(), DrawActivity::class.java))
            }
            addInit.setOnClickListener {
                DrawActivity.isSign = false
                DrawActivity.isAccountScreen = true
                DrawActivity.listener = object : OnDrawSaveListener {
                    override fun saveImageBitmap(bitmap: Bitmap) {
                        initImage.setImageBitmap(bitmap)
                    }

                    override fun saveImageViewHolder(viewHolder: SignatureUtils.ViewHolder) {
                    }
                }
                startActivity(Intent(requireContext(), DrawActivity::class.java))
            }
            backBtn.setOnClickListener { navController.popBackStack() }
            changePassword.setOnClickListener {
                navController.navigate(R.id.navigation_change_password)
            }
        }
    }

    private fun initData() {
        with(binding) {
            with(DeftApp.user) {
                nameText.text = name
                emailText.text = email
                val userInitialsImage = thisView.findViewById<TextView>(R.id.document_image)
                val userInitials = thisView.findViewById<TextView>(R.id.document_name)
                val userDate = thisView.findViewById<TextView>(R.id.document_date)

                userInitials.text = email
                userDate.text = createdAt
                userInitialsImage.text = Util.getInitialsLetter().toUpperCase(Locale.getDefault())

                if (sign?.url.isNullOrEmpty()) {
                    isSignEmpty(true)
                } else {
                    isSignEmpty(false)
                    Glide.with(activity)
                        .load(Util.DATA_URL+sign?.url)
                        .into(signImage)
                    signUpdate.setOnClickListener {
                        DrawActivity.isSign = true
                        DrawActivity.isAccountScreen = true
                        DrawActivity.listener = object : OnDrawSaveListener {
                            override fun saveImageBitmap(bitmap: Bitmap) {
                                signImage.setImageBitmap(bitmap)
                            }

                            override fun saveImageViewHolder(viewHolder: SignatureUtils.ViewHolder) {
                            }
                        }
                        startActivity(Intent(requireContext(), DrawActivity::class.java))
                    }
                    signDelete.setOnClickListener {
                        sign?.id?.let {
                            deleteDocument(it, "sign")
                        }
                    }
                }
                if (initials?.url.isNullOrEmpty()) {
                    isInitialEmpty(true)
                } else {
                    isInitialEmpty(false)
                    Glide.with(activity)
                        .load(Util.DATA_URL+initials?.url)
                        .into(initImage)
                    initUpdate.setOnClickListener {
                        DrawActivity.isSign = false
                        DrawActivity.isAccountScreen = true
                        DrawActivity.listener = object : OnDrawSaveListener {
                            override fun saveImageBitmap(bitmap: Bitmap) {
                                initImage.setImageBitmap(bitmap)
                            }

                            override fun saveImageViewHolder(viewHolder: SignatureUtils.ViewHolder) {
                            }
                        }
                        startActivity(Intent(requireContext(), DrawActivity::class.java))

                        initDelete.setOnClickListener {
                            initials?.id?.let {
                                deleteDocument(it, "initials")
                            }
                        }
                    }
                }
            }
        }
    }

    private fun deleteDocument(id: Int, type: String) {
        if (type == "initials") {
            DeleteInitialsDialog.display(activity.supportFragmentManager) {
                sendRequestDelete(id, type)
            }
        } else {
            DeleteSignedDialog.display(activity.supportFragmentManager) {
                sendRequestDelete(id, type)
            }
        }
    }

    private fun sendRequestDelete(id: Int, type: String) {
        val deletePresenter = SignDeletePresenter(activity)
        deletePresenter.attachView(object : BaseModelView {
            override fun requestSuccess(data: BaseModel) {
                if (type == "initials") {
                    isInitialEmpty(true)
                    DeftApp.user.initials = null
                } else {
                    isSignEmpty(true)
                    DeftApp.user.sign = null
                }
            }
        })
        deletePresenter.sendResponse(
            DeftApp.user.apiToken!!,
            id.toString()
        )
    }

    private fun isSignEmpty(value: Boolean) {
        val visibility = if (value) View.GONE else View.VISIBLE

        binding.signImage.visibility = visibility
        binding.signDelete.visibility = visibility
        binding.signUpdate.visibility = visibility
        binding.signEmpty.visibility = if (!value) View.GONE else View.VISIBLE
    }

    private fun isInitialEmpty(value: Boolean) {
        val visibility = if (value) View.GONE else View.VISIBLE

        binding.initImage.visibility = visibility
        binding.initDelete.visibility = visibility
        binding.initUpdate.visibility = visibility
        binding.initEmpty.visibility = if (!value) View.GONE else View.VISIBLE
    }

    override fun getUserSuccess(data: UserModel) {
        DeftApp.user = data.data!!
        initData()
    }

}