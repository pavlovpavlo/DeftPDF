package com.sign.deftpdf.ui.main

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
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
import com.google.android.material.navigation.NavigationBarMenuView
import com.google.android.material.navigation.NavigationBarView
import com.sign.deftpdf.DeftApp
import com.sign.deftpdf.R
import com.sign.deftpdf.base.BaseActivity
import com.sign.deftpdf.databinding.ActivityMainBinding
import com.sign.deftpdf.model.BaseModel
import com.sign.deftpdf.ui.check_auth.CheckAuthActivity
import com.sign.deftpdf.ui.main.menu.MenuAdapter
import com.sign.deftpdf.ui.main.menu.Top
import com.sign.deftpdf.util.FileUtil
import com.sign.deftpdf.util.LocalSharedUtil
import com.sign.deftpdf.util.Util
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*


class MainActivity : BaseActivity(R.layout.activity_main), StoreDocumentView {
    private var restart=false;
    lateinit var mDrawer: DrawerLayout
    private lateinit var settingsBtn: AppCompatButton
    private lateinit var navController: NavController
    private var lastDestinationId : Int = R.id.navigation_home
    private val presenter: StoreDocumentPresenter = StoreDocumentPresenter(this)
    private val listener =
        NavController.OnDestinationChangedListener { controller, destination, arguments ->
            if (destination.id == R.id.navigation_home
                || destination.id == R.id.navigation_documents
                || destination.id == R.id.navigation_library
            ) {
                binding.addDocument.visibility = View.VISIBLE
            } else {
                binding.addDocument.visibility = View.GONE
            }
        }



    internal val binding by viewBinding(ActivityMainBinding::bind)
    private val pickPDF =
        registerForActivityResult(ActivityResultContracts.GetContent()) { contentUri ->
            if (contentUri != null)
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
        Log.d("Load-Start-Activity", "Load")
    }

    override fun startLoader() {
        Log.d("Load-Start", "Load")
        try {
            findViewById<View>(R.id.progress_main).visibility = View.VISIBLE
        } catch (e: Exception) {
            //not cast
        }
    }

    override fun stopLoader() {
        Log.d("Load-Stop", "Load")
        findViewById<View>(R.id.progress_main).visibility = View.GONE
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
        val file: File = FileUtil.from(this, uri)

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
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("market://details?id=$packageName")
                                )
                            )
                        } catch (e: ActivityNotFoundException) {
                            startActivity(
                                Intent(
                                    Intent.ACTION_VIEW,
                                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                                )
                            )
                        }
                    }
                    2 -> {
                        navController.navigate(R.id.navigation_help)
                    }
                    3 -> {
                        val selectorIntent = Intent(Intent.ACTION_SENDTO)
                        selectorIntent.data = Uri.parse("mailto:")

                        val emailIntent = Intent(Intent.ACTION_SEND)
                        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(DeftApp.user.email))
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact Support")
                        emailIntent.selector = selectorIntent

                        startActivity(
                            Intent.createChooser(
                                emailIntent,
                                "Send email..."
                            )
                        )

                    }
                }
            }

            override fun onItemLongClick(position: Int, v: View?) {}
        }


    override fun onRestart() {
       restart=true;
       super.onRestart()
        Log.d("RESTART", "GO")
    }

    override fun onResume() {
        super.onResume()
        if(restart){
            navController.navigate(lastDestinationId)
         restart=false;
        }
        navController.addOnDestinationChangedListener(listener)
    }

    override fun onPause() {
        lastDestinationId = navController.currentDestination!!.id
        navController.removeOnDestinationChangedListener(listener)
        super.onPause()
    }


    private fun refreshCurrentFragment(){
        val id = navController.currentDestination?.id
        navController.popBackStack(id!!, true)
        navController.navigate(id)
    }

    override fun storeDocumentSuccess(data: BaseModel) {
        when (navController.currentDestination?.id) {
            R.id.navigation_documents -> {
                navController.navigate(R.id.navigation_documents)
            }
            R.id.navigation_home -> {
                navController.navigate(R.id.navigation_home)
            }
            R.id.navigation_library -> {
                navController.navigate(R.id.navigation_library)
            }
        }
    }
}