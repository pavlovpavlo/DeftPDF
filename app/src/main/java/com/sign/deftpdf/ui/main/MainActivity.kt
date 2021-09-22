package com.sign.deftpdf.ui.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.DeftApp
import com.sign.deftpdf.R
import com.sign.deftpdf.base.BaseActivity
import com.sign.deftpdf.databinding.ActivityMainBinding
import com.sign.deftpdf.model.BaseModel
import com.sign.deftpdf.ui.check_auth.CheckAuthActivity
import com.sign.deftpdf.ui.main.menu.MenuAdapter
import com.sign.deftpdf.ui.main.menu.Top
import com.sign.deftpdf.util.LocalSharedUtil
import com.sign.deftpdf.util.Util
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*


class MainActivity : BaseActivity(R.layout.activity_main), StoreDocumentView {

    lateinit var mDrawer: DrawerLayout
    private lateinit var settingsBtn: AppCompatButton
    private lateinit var navController: NavController
    private val presenter: StoreDocumentPresenter = StoreDocumentPresenter(this)

    internal val binding by viewBinding(ActivityMainBinding::bind)
    private val pickPDF = registerForActivityResult(ActivityResultContracts.GetContent()) { contentUri ->
        loadPdfToServer(contentUri)
    }

    var recycler_menu: RecyclerView? = null
    var menuAdapter: MenuAdapter? = null
    val list_menu: ArrayList<Top> =
            ArrayList<Top>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        NavigationUI.setupWithNavController(binding.appTablayout, navController)

        initMenu()
        initListeners()
    }

    private fun initMenu() {
        mDrawer = findViewById(R.id.drawer_layout)
        settingsBtn = findViewById(R.id.btn_setting)
        recycler_menu = findViewById(R.id.r_menu)
        val signOut = findViewById<AppCompatButton>(R.id.sign_out)
        val userInitialsImage = findViewById<TextView>(R.id.user_initials)
        val userInitials = findViewById<TextView>(R.id.user_initials_text)

        userInitials.text = DeftApp.user.name
        userInitialsImage.text = Util.getInitialsLetter().toUpperCase(Locale.getDefault())

        recycler_menu!!.layoutManager = GridLayoutManager(this, 1)
        menuAdapter = MenuAdapter(
                list_menu as ArrayList<Top>?,
                this
        )
        recycler_menu!!.adapter = menuAdapter
        menuAdapter!!.setOnItemClickListener(onResultItemClick)
        recycler_menu!!.isNestedScrollingEnabled = true

        var menu_item = Top()
        menu_item.text = "Signatures & Initials"
        menu_item.img_r = R.drawable.ic_menu__1
        list_menu.add(menu_item)
        var menu_item2 = Top()
        menu_item2.text = "Rate Us"
        menu_item2.img_r = R.drawable.ic_menu_2
        list_menu.add(menu_item2)
        var menu_item3 = Top()
        menu_item3.text = "Help"
        menu_item3.img_r = R.drawable.ic_menu_3
        list_menu.add(menu_item3)
        var menu_item4 = Top()
        menu_item4.text = "Contact Support"
        menu_item4.img_r = R.drawable.ic_menu_4
        list_menu.add(menu_item4)


        menuAdapter = MenuAdapter(
                list_menu as ArrayList<Top?>?,
                this
        )
        signOut.setOnClickListener {
            LocalSharedUtil().setTokenParameter("", this)
            startActivity(Intent(this, CheckAuthActivity::class.java))
            this.finishAffinity()
        }
        recycler_menu!!.adapter = menuAdapter
        menuAdapter!!.notifyDataSetChanged()
    }



    private fun initListeners() {
        with(binding) {
            addDocument.setOnClickListener { pickPDF.launch(Util.MIMETYPE_PDF) }
        }
        settingsBtn.setOnClickListener {
            mDrawer.closeDrawers()
            navController.navigate(R.id.navigation_account_setting)
        }
    }

    private fun loadPdfToServer(uri: Uri) {
        presenter.attachView(this)
        val file: File = Util.getFile(this, uri)

        val requestFile = RequestBody.create(MediaType.parse("file"), file)

        val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
        presenter.sendResponse(DeftApp.user.apiToken!!, body)
    }

    private val onResultItemClick: MenuAdapter.ClickListener =
            object : MenuAdapter.ClickListener {
                override fun onItemClick(position: Int, v: View?) {
                    if (mDrawer.isDrawerOpen(GravityCompat.START)) {
                        mDrawer.closeDrawers()
                    }
                    when (position) {
                        0 -> {
                            navController.navigate(R.id.navigation_account_setting)
                        }
                        1 -> {
                            try {
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
                            } catch (e: ActivityNotFoundException) {
                                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
                            }
                        }
                        2 -> {
                            navController.navigate(R.id.navigation_help)
                        }
                        3 -> {
                            val intent = Intent(Intent.ACTION_SENDTO)
                            intent.data = Uri.parse("mailto:")
                            intent.putExtra(Intent.EXTRA_EMAIL, DeftApp.user.email)
                            if (intent.resolveActivity(packageManager) != null) {
                                startActivity(intent)
                            }
                        }
                    }
                }

                override fun onItemLongClick(position: Int, v: View?) {}
            }

    override fun storeDocumentSuccess(data: BaseModel) {

    }
}