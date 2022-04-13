package com.abhi.food_app.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.abhi.food_app.R
import com.abhi.food_app.fragments.FaqFragment
import com.abhi.food_app.fragments.FavouritesFragment
import com.abhi.food_app.fragments.HomeFragment
import com.abhi.food_app.fragments.ProfileFragment
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    lateinit var cordinatorLayout: CoordinatorLayout
    lateinit var drawerLayout: DrawerLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView

    var previousMenuItem:MenuItem?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cordinatorLayout = findViewById(R.id.cordinatorLayout)
        drawerLayout = findViewById(R.id.drawerLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)

        setUpToolbar()

        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity, drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        openHome()

        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem!=null){
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it
            when (it.itemId) {
                R.id.menuHome -> {
                    openHome()
                }
                R.id.menuFav -> {
                    Toast.makeText(this, "Favourites", Toast.LENGTH_SHORT).show()
                    supportActionBar?.title = "Favourite"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FavouritesFragment())
                        .commit()
                    drawerLayout.closeDrawers()
                }
                R.id.menuprofile -> {
                    Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                    supportActionBar?.title = "Profile"
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, ProfileFragment())
                        .commit()
                    drawerLayout.closeDrawers()
                }
                R.id.menufaq -> {
                    supportActionBar?.title = "FAQ"
                    Toast.makeText(this, "FAQ Page", Toast.LENGTH_SHORT).show()
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FaqFragment())
                        .commit()
                    drawerLayout.closeDrawers()
                }
                R.id.menulogout -> {
                    val dialog = AlertDialog.Builder(this)
                    dialog.setTitle("CONFIRMATION")
                    dialog.setMessage("Are you sure you want to log out?")
                    dialog.setPositiveButton("yes") { text, listener ->
                        val i = Intent(this,LoginActivity::class.java)
                        startActivity(i)
                        this.finish()
                        Toast.makeText(this, "you have successfully logout", Toast.LENGTH_SHORT).show()
                    }
                    dialog.setNegativeButton("no") { text, listener ->
                        startActivity(Intent(this,HomeFragment::class.java))
                    }
                    dialog.create()
                    dialog.show()
                                   }
            }
            return@setNavigationItemSelectedListener true
        }


    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Home"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun openHome() {
        Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
        supportActionBar?.title = "Home"
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame, HomeFragment())
            .commit()
        drawerLayout.closeDrawers()
        navigationView.setCheckedItem(R.id.menuHome)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)
        when (frag) {
            !is HomeFragment ->openHome()
            else ->super.onBackPressed()

        }

    }
}