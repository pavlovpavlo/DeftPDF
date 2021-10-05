package com.sign.deftpdf.ui.account

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.DeftApp
import com.sign.deftpdf.R
import com.sign.deftpdf.base.BaseFragment
import com.sign.deftpdf.databinding.*
import com.sign.deftpdf.model.user.UserModel
import com.sign.deftpdf.ui.check_auth.CheckAuthActivity
import com.sign.deftpdf.ui.main.GetUserView
import com.sign.deftpdf.ui.main.MainActivity
import com.sign.deftpdf.ui.subscription.SubscriptionActivity
import com.sign.deftpdf.util.LocalSharedUtil
import com.sign.deftpdf.util.Util
import java.text.SimpleDateFormat
import java.util.*

class AccountFragment : BaseFragment(R.layout.fragment_account), GetUserView {

    private val binding by viewBinding(FragmentAccountBinding::bind)
    private lateinit var navController: NavController
    private lateinit var activity: MainActivity
    private lateinit var presenter: CancelSubscribePresenter
    private lateinit var thisView: View

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity)
            activity = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navController = NavHostFragment.findNavController(this@AccountFragment)
        presenter = CancelSubscribePresenter(activity)
        presenter.attachView(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thisView = view
        initViews()
    }

    private fun initViews(){
        binding.signOut.setOnClickListener {
            LocalSharedUtil().setTokenParameter("", requireContext())
            startActivity(Intent(requireContext(), CheckAuthActivity::class.java))
            requireActivity().finishAffinity()
        }
        DeftApp.user.subscription?.let {
            if(it.status){
                binding.countSubscription.text = "${it.limitPaidDocuments} documents left"
                binding.textSubscription.text = getString(R.string.premium_account)
            }else{
                binding.textSubscription.text = getString(R.string.upgrade_account)
                binding.countSubscription.text = "${it.freeDocument} documents left"
            }
            binding.titleSubscription.text = "${it.type.capitalize()} Account"
            binding.upgradeSubscription.setOnClickListener {
                startActivity(Intent(activity, SubscriptionActivity::class.java))
            }
            if(it.type == "premium" || it.type == "business"){
                binding.cancelSubscribe.visibility = View.VISIBLE
                binding.cancelSubscribe.text = "Cancel ${it.type.capitalize()} Account"
                binding.cancelSubscribe.setOnClickListener {
                    presenter.sendResponse(DeftApp.user.apiToken.toString())
                }
            }else{
                binding.cancelSubscribe.visibility = View.GONE
            }
        }

        binding.securityTab.setOnClickListener {
            navController.navigate(R.id.navigation_change_password)
        }
        binding.burgerBtn.setOnClickListener {
            openMenu()
        }
        binding.notificationMenu.setOnClickListener {
            navController.navigate(R.id.navigation_notification)
        }

        val userInitialsImage = thisView.findViewById<TextView>(R.id.document_image)
        val userInitials = thisView.findViewById<TextView>(R.id.document_name)
        val userDate = thisView.findViewById<TextView>(R.id.document_date)

        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        calendar.time = sdf.parse(DeftApp.user.createdAt)

        sdf.timeZone = TimeZone.getDefault()
        val date = sdf.format(calendar.time)

        userInitials.text = DeftApp.user.email
        userDate.text = date
        userInitialsImage.text = Util.getInitialsLetter().toUpperCase(Locale.getDefault())
    }

    override fun getUserSuccess(data: UserModel) {
        Toast.makeText(requireContext(), "Your ${DeftApp.user.subscription!!.type} subscription was cancelled", Toast.LENGTH_SHORT).show()
        DeftApp.user.subscription = data.data!!.subscription
        initViews()
    }
}