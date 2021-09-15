package com.sign.deftpdf.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.sign.deftpdf.R
import com.sign.deftpdf.databinding.ActivityMainBinding
import java.util.ArrayList

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    var mDrawer: DrawerLayout? = null
    private lateinit var navController: NavController

    private val binding by viewBinding(ActivityMainBinding::bind)

    var recycler_menu: RecyclerView? = null
    var adapter__menu: adapter_menu? = null
    val list_menu: ArrayList<Top> =
        ArrayList<Top>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        NavigationUI.setupWithNavController(binding.appTablayout, navController)

        mDrawer = findViewById<DrawerLayout?>(R.id.drawer_layout)
        recycler_menu = findViewById(R.id.r_menu)
        recycler_menu!!.setLayoutManager(GridLayoutManager(this, 1))
        adapter__menu = adapter_menu(
            list_menu as ArrayList<Top?>?,
            this
        )
        recycler_menu!!.setAdapter(adapter__menu)
        adapter__menu!!.setOnItemClickListener(OnResultItemClick)
        recycler_menu!!.setNestedScrollingEnabled(true)

       var menu_item = Top();
        menu_item.text="Signatures & Initials"
        menu_item.img_r=R.drawable.ic_menu__1
        list_menu.add(menu_item)
        var menu_item2 = Top();
        menu_item2.text="Rate Us"
        menu_item2.img_r=R.drawable.ic_menu_2
        list_menu.add(menu_item2)
        var menu_item3 = Top();
        menu_item3.text="Help"
        menu_item3.img_r=R.drawable.ic_menu_3
        list_menu.add(menu_item3)
        var menu_item4 = Top();
        menu_item4.text="Contact Support"
        menu_item4.img_r=R.drawable.ic_menu_4
        list_menu.add(menu_item4)


        adapter__menu = adapter_menu(
            list_menu as ArrayList<Top?>?,
            this
        )
        recycler_menu!!.adapter = adapter__menu
        adapter__menu!!.notifyDataSetChanged();


    }

    private val OnResultItemClick: adapter_menu.ClickListener =
        object : adapter_menu.ClickListener {
            override fun onItemClick(position: Int, v: View?) {
                if (mDrawer!!.isDrawerOpen(GravityCompat.START)) {
                    mDrawer!!.closeDrawers()
                }
            }
            override fun onItemLongClick(position: Int, v: View?) {}
        }

}